package com.a.server.data.repository

import arrow.core.Either
import com.a.server.core.util.catchAndDispatch
import com.a.server.data.local.room.ApplicationDatabase
import com.a.server.data.mapper.toEntity
import com.a.server.data.mapper.toFileModel
import com.a.server.data.mapper.toModel
import com.a.server.data.mapper.toSortedFileModel
import com.a.server.domain.model.storage.FileModel
import com.a.server.domain.model.storage.FolderModel
import com.a.server.domain.repository.SettingsRepository
import com.a.server.domain.repository.StorageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import org.koin.core.annotation.Single
import java.io.File

@Single
class StorageRepositoryImpl(
    private val settingsRepository: SettingsRepository,
    private val applicationDatabase: ApplicationDatabase
): StorageRepository {

    override fun createFolder(folderName: String): Flow<Either<String, String>> {
        return flow {
            val rootPath = settingsRepository.storagePath.first()
            val targetFolder = File(rootPath, folderName)

            if (targetFolder.exists()) {
                emit(Either.Left("Folder Already Exists"))
                return@flow
            }

            targetFolder.mkdirs()
            emit(Either.Right("Folder Created"))
        }.catchAndDispatch()
    }

    override fun getFolderList(): Flow<Either<String, List<FolderModel>>> {
        return flow<Either<String, List<FolderModel>>> {
            applicationDatabase.folderDao.getList().collect { folderEntities ->
                val folderModels = folderEntities.map { folderEntity ->
                    folderEntity.toModel()
                }
                emit(Either.Right(folderModels))
            }
        }.catchAndDispatch()
    }

    override fun getFolderSingle(folderName: String): Flow<Either<String, FolderModel>> {
        return flow {
            applicationDatabase.folderDao.getSingle(
                name = folderName
            ).collect { folderEntity ->
                if (folderEntity != null) {
                    emit(Either.Right(folderEntity.toModel()))
                } else {
                    emit(Either.Left("Folder Does Not Exist"))
                }
            }
        }.catchAndDispatch()
    }

    override fun upsertList(folderModels: List<FolderModel>): Flow<Either<String, String>> {
        return flow<Either<String, String>> {
            applicationDatabase.folderDao.upsertList(
                folders = folderModels.map { folderEntity ->
                    folderEntity.toEntity()
                }
            )

            val storagePath = settingsRepository.storagePath.first()
            folderModels.forEach { folderModel ->
                File(storagePath, folderModel.name).apply {
                    if (!exists()) mkdirs()
                }
            }

            emit(Either.Right("Folders Created"))
        }.catchAndDispatch()
    }

    override fun deleteList(folderNames: List<String>): Flow<Either<String, String>> {
        return flow<Either<String, String>> {
            applicationDatabase.folderDao.deleteList(
                names = folderNames
            )

            val storagePath = settingsRepository.storagePath.first()
            folderNames.forEach { folderName ->
                File(storagePath, folderName).apply {
                    if (exists()) deleteRecursively()
                }
            }

            emit(Either.Right("Folders Deleted"))
        }.catchAndDispatch()
    }

    override fun getFileList(folderName: String): Flow<Either<String, List<FileModel>>> {
        return flow<Either<String, List<FileModel>>> {
            val storagePath = settingsRepository.storagePath.first()
            val serverHost = settingsRepository.serverHost.first()
            val serverPort = settingsRepository.serverPort.first()

            val localDirectUrl = "https://$serverHost:$serverPort/$folderName"

            val fileList = File(storagePath, folderName).listFiles().toSortedFileModel(
                localDirectUrl = localDirectUrl,
                remoteDirectUrl = ""
            )

            emit(Either.Right(fileList))
        }.catchAndDispatch()
    }

    override fun getFileSingle(filePath: String): Flow<Either<String, FileModel>> {
        return flow<Either<String, FileModel>> {
            val storagePath = settingsRepository.storagePath.first()
            val serverHost = settingsRepository.serverHost.first()
            val serverPort = settingsRepository.serverPort.first()

            val localDirectUrl = "https://$serverHost:$serverPort/$filePath"

            val file = File(storagePath, filePath).toFileModel(
                localDirectUrl = localDirectUrl,
                remoteDirectUrl = ""
            )

            emit(Either.Right(file))
        }.catchAndDispatch()
    }

}