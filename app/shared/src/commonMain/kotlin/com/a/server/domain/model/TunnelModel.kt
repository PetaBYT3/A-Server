package com.a.server.domain.model

data class TunnelModel(
    val isRunning: Boolean = false,
    val uptime: Long? = 0L,
    val publicUrl: String? = null
)