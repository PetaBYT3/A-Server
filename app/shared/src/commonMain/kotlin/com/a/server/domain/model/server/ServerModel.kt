package com.a.server.domain.model.server

data class ServerModel(
    val isRunning: Boolean = false,
    val uptime: Long = 0L
)
