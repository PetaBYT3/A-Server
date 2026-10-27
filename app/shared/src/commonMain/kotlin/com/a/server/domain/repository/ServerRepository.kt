package com.a.server.domain.repository

import kotlinx.coroutines.flow.StateFlow

interface ServerRepository {
    val isRunning: StateFlow<Boolean>
    val logs: StateFlow<List<String>>

    fun start()
    fun stop()
}