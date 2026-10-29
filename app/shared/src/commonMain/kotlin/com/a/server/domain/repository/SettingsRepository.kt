package com.a.server.domain.repository

import arrow.core.Either
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface SettingsRepository {
    val serverPort: StateFlow<Int>
    val serverHost: StateFlow<String>
    fun setServerPortAndHost(port: Int, host: String): Flow<Either<String, String>>
}