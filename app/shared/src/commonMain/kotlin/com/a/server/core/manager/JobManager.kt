package com.a.server.core.manager

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock


class JobManager(
    private val scope: CoroutineScope
) {
    private val activeJobs = mutableMapOf<String, Job>()
    private val mutex = Mutex()

    private val _activeJobKeys = MutableStateFlow<Set<String>>(emptySet())
    val activeJobKeys: StateFlow<Set<String>> = _activeJobKeys.asStateFlow()

    fun launchJob(
        key: String,
        block: suspend CoroutineScope.() -> Unit
    ) {
        scope.launch {
            mutex.withLock {
                activeJobs[key]?.cancel()
            }

            val newJob = scope.launch(block = block)

            mutex.withLock {
                activeJobs[key] = newJob
                updateActiveKeysLocked()
            }

            newJob.invokeOnCompletion {
                scope.launch {
                    mutex.withLock {
                        activeJobs.remove(key)
                        updateActiveKeysLocked()
                    }
                }
            }
        }
    }

    suspend fun stopJob(key: String) {
        mutex.withLock {
            activeJobs[key]?.cancel()
            activeJobs.remove(key)
            updateActiveKeysLocked()
        }
    }

    suspend fun stopAllJobs() {
        mutex.withLock {
            activeJobs.values.forEach { job ->
                job.cancel()
            }
            activeJobs.clear()
            updateActiveKeysLocked()
        }
    }

    fun isJobActive(key: String): Flow<Boolean> {
        return activeJobKeys.map { keys ->
            keys.contains(key)
        }.distinctUntilChanged()
    }

    private fun updateActiveKeysLocked() {
        _activeJobKeys.update {
            activeJobs.filterValues { job ->
                job.isActive
            }.keys.toSet()
        }
    }
}