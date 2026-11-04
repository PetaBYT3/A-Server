package com.a.server.presentation.dashboard.main

import com.a.server.presentation.model.server.ServerUi
import com.a.server.presentation.model.storage.StorageUi
import com.a.server.presentation.model.tunnel.TunnelUi

data class MainState(
    val serverState: ServerUi = ServerUi(),
    val serverPort: String = "",
    val serverHost: String = "",
    val isServerLoading: Boolean = false,

    val tunnelState: TunnelUi = TunnelUi(),
    val isTunnelLoading: Boolean = false,

    val authenticationSize: Int = 0,

    val storageState: StorageUi = StorageUi(),
    val storagePath: String = ""
)
