package com.a.server.domain.repository

import kotlinx.coroutines.flow.StateFlow

interface ServerRepository {
    val isRunning: StateFlow<Boolean>

    fun start()
    fun stop()
}