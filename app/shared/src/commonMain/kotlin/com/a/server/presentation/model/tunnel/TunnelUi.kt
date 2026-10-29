package com.a.server.presentation.model.tunnel

data class TunnelUi(
    val isRunning: Boolean = false,
    val uptime: String = "00:00:00, 00 Days",
    val publicUrl: String? = null
)
