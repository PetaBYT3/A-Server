package com.a.server.data.local.preferences

import kotlinx.coroutines.flow.StateFlow

interface SettingsApi {
    val serverPort: StateFlow<Int>
    suspend fun setServerPort(port: Int)

    val serverHost: StateFlow<String>
    suspend fun setServerHost(host: String)
}