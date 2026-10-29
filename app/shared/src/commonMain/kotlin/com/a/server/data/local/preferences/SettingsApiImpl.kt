@file:OptIn(ExperimentalSettingsApi::class)

package com.a.server.data.local.preferences

import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.coroutines.getIntStateFlow
import com.russhwolf.settings.coroutines.getStringStateFlow
import com.russhwolf.settings.set
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.StateFlow
import org.koin.core.annotation.Single

@Single
class SettingsApiImpl(
    private val observableSettings: ObservableSettings
): SettingsApi {
    private val settingsScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    
    private companion object {
        const val SERVER_PORT = "server_port"
        const val SERVER_HOST = "server_host"
    }

    override val serverPort: StateFlow<Int> = observableSettings.getIntStateFlow(
        coroutineScope = settingsScope,
        key = SERVER_PORT,
        defaultValue = 8080
    )

    override suspend fun setServerPort(port: Int) {
        observableSettings.set(
            key = SERVER_PORT,
            value = port
        )
    }

    override val serverHost: StateFlow<String> = observableSettings.getStringStateFlow(
        coroutineScope = settingsScope,
        key = SERVER_HOST,
        defaultValue = "0.0.0.0"
    )

    override suspend fun setServerHost(host: String) {
        observableSettings.set(
            key = SERVER_HOST,
            value = host
        )
    }
}