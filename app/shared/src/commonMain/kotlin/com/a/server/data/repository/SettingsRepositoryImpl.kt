package com.a.server.data.repository

import arrow.core.Either
import com.a.server.data.local.preferences.SettingsApi
import com.a.server.data.util.catchAndDispatch
import com.a.server.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import org.koin.core.annotation.Single

@Single
class SettingsRepositoryImpl(
    private val settingsApi: SettingsApi
): SettingsRepository {
    override val serverPort: StateFlow<Int> = settingsApi.serverPort
    override val serverHost: StateFlow<String> = settingsApi.serverHost

    override fun setServerPortAndHost(
        port: Int,
        host: String
    ): Flow<Either<String, String>> {
        return flow {
            settingsApi.setServerPort(port)
            settingsApi.setServerHost(host)
            emit(Either.Right("Server Configuration Saved"))
        }.catchAndDispatch()
    }
}