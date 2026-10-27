package com.a.server.data.server

import kotlinx.coroutines.flow.StateFlow

interface CloudflareManager {
    val logs: StateFlow<List<String>>

    fun start(port: Int = 8080)
    fun stop()
}