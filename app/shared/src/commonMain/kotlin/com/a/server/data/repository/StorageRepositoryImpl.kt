package com.a.server.data.repository

import arrow.core.Either
import com.a.server.core.manager.SystemManager
import com.a.server.core.util.calculateSize
import com.a.server.core.util.catchAndDispatch
import com.a.server.data.local.room.ApplicationDatabase
import com.a.server.data.mapper.toEntity
import com.a.server.data.mapper.toFileModel
import com.a.server.data.mapper.toModel
import com.a.server.data.mapper.toSortedFileModel
import com.a.server.domain.model.storage.FileModel
import com.a.server.domain.model.storage.FolderModel
import com.a.server.domain.model.storage.StorageModel
import com.a.server.domain.repository.SettingsRepository
import com.a.server.domain.repository.StorageRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.koin.core.annotation.Single
import java.io.File
import kotlin.time.Duration.Companion.seconds

@Single
class StorageRepositoryImpl(
    private val settingsRepository: SettingsRepository,
    private val applicationDatabase: ApplicationDatabase
): StorageRepository {

    private val repositoryScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private val _currentPath = MutableStateFlow("")
    override val currentPath: StateFlow<String> = _currentPath.asStateFlow()

    private val _files = MutableStateFlow<List<FileModel>>(emptyList())
    override val files: StateFlow<List<FileModel>> = _files.asStateFlow()

    override val state: StateFlow<StorageModel> = flow {
        while (currentCoroutineContext().isActive) {
            val systemManager = SystemManager()
            val usedSize = File(settingsRepository.storagePath.first()).calculateSize()
            val freeSize = systemManager.getFreeSize()

            emit(StorageModel(usedSize, freeSize))
            delay(5.seconds)
        }
    }.stateIn(
        scope = repositoryScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = StorageModel()
    )

    init {
        repositoryScope.launch {
            initFile()
        }
    }

    private suspend fun initFile() {
        val storagePath = settingsRepository.storagePath.first { it.isNotBlank() }
        _currentPath.update { storagePath }

        val files = File(storagePath).listFiles().toSortedFileModel()
        _files.update { files }
    }

    override suspend fun upFolder() {
        val storagePath = settingsRepository.storagePath.first()
        val currentPathFile = File(_currentPath.value)
        val targetPathFile = currentPathFile.parentFile ?: return

        if (currentPathFile.absolutePath != storagePath && targetPathFile.absolutePath.startsWith(storagePath)) {
            _currentPath.update { targetPathFile.absolutePath }

            val files = targetPathFile.listFiles().toSortedFileModel()
            _files.update { files }
        }
    }

    override suspend fun openFolder(folderName: String) {
        val targetPathFile = File(_currentPath.value, folderName)

        if (targetPathFile.exists() && targetPathFile.isDirectory) {
            _currentPath.update { targetPathFile.absolutePath }

            val files = targetPathFile.listFiles().toSortedFileModel()
            _files.update { files }
        }
    }

    override suspend fun getFileDetail(targetPath: String): Flow<Either<String, FileModel>> {
        return flow<Either<String, FileModel>> {
            val targetFile = File(targetPath).toFileModel()
            emit(Either.Right(targetFile))
        }.catchAndDispatch()
    }

    override fun createFolder(folderName: String): Flow<Either<String, String>> {
        return flow {
            val targetFolder = File(_currentPath.value, folderName)

            if (targetFolder.exists()) {
                emit(Either.Left("Folder Already Exists"))
                return@flow
            }

            targetFolder.mkdirs()

            val files = File(_currentPath.value).listFiles().toSortedFileModel()
            _files.update { files }
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

    override fun getFolderSingle(name: String): Flow<Either<String, FolderModel>> {
        return flow {
            applicationDatabase.folderDao.getSingle(
                name = name
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
            val fileList = File(storagePath, folderName).listFiles().toSortedFileModel()

            emit(Either.Right(fileList))
        }.catchAndDispatch()
    }

}