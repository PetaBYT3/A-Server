package com.a.server.data.repository

import arrow.core.Either
import com.a.server.core.manager.SystemManager
import com.a.server.core.util.calculateSize
import com.a.server.core.util.catchAndDispatch
import com.a.server.data.mapper.toFileModel
import com.a.server.data.mapper.toSortedFileModel
import com.a.server.domain.model.storage.FileModel
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
    private val settingsRepository: SettingsRepository
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
}