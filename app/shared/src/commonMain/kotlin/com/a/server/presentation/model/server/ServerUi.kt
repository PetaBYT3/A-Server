package com.a.server.presentation.model.server

data class ServerUi(
    val isRunning: Boolean = false,
    val uptime: String = "00:00:00, 00 Days"
)