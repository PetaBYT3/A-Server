package com.a.server.data.repository

import com.a.server.core.manager.SystemManager
import com.a.server.core.util.calculateSize
import com.a.server.domain.model.storage.StorageModel
import com.a.server.domain.repository.SettingsRepository
import com.a.server.domain.repository.StorageRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.isActive
import org.koin.core.annotation.Single
import java.io.File
import kotlin.time.Duration.Companion.seconds

@Single
class StorageRepositoryImpl(
    private val settingsRepository: SettingsRepository
): StorageRepository {
    override val state: StateFlow<StorageModel> = flow {
        while (currentCoroutineContext().isActive) {
            val systemManager = SystemManager()
            val usedSize = File(settingsRepository.storagePath.first()).calculateSize()
            val freeSize = systemManager.getFreeSize()

            emit(StorageModel(usedSize, freeSize))
            delay(5.seconds)
        }
    }.stateIn(
        scope = CoroutineScope(SupervisorJob() + Dispatchers.IO),
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = StorageModel()
    )
}