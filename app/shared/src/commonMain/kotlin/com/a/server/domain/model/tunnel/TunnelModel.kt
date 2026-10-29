package com.a.server.domain.model.tunnel

data class TunnelModel(
    val isRunning: Boolean = false,
    val uptime: Long? = 0L,
    val publicUrl: String? = null
)