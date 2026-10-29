package com.a.server.presentation.dashboard

import com.a.server.domain.model.TunnelModel

data class DashboardState(
    val isServerRunning: Boolean = false,
    val serverUptime: String = "00:00:00, 0 Days",
    val serverPort: String = "",
    val serverHost: String = "",

    val tunnelModel: TunnelModel = TunnelModel()
)
