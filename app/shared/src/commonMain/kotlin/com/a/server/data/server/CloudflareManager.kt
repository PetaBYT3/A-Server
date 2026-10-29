package com.a.server.data.server

import kotlinx.coroutines.flow.StateFlow

interface CloudflareManager {
    val publicUrl: StateFlow<String?>

    fun start(port: Int)
    fun stop()
}