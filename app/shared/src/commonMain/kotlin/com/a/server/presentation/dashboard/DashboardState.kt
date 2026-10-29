package com.a.server.presentation.dashboard

import com.a.server.presentation.model.server.ServerUi
import com.a.server.presentation.model.tunnel.TunnelUi

data class DashboardState(
    val serverState: ServerUi = ServerUi(),
    val serverPort: String = "",
    val serverHost: String = "",
    val isServerLoading: Boolean = false,

    val tunnelState: TunnelUi = TunnelUi(),
    val isTunnelLoading: Boolean = false
)
