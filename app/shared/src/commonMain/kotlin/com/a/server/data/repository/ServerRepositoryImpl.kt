package com.a.server.data.repository

import com.a.server.data.server.CloudflareManager
import com.a.server.data.server.Ktor
import com.a.server.domain.repository.ServerRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.koin.core.annotation.Single

@Single
class ServerRepositoryImpl(
    private val ktor: Ktor,
    private val cloudflare: CloudflareManager
): ServerRepository {
    private val _isRunning = MutableStateFlow(false)
    override val isRunning: StateFlow<Boolean> = _isRunning.asStateFlow()

    override val logs: StateFlow<List<String>> = cloudflare.logs

    override fun start() {
        ktor.start()
        cloudflare.start()

        _isRunning.update { true }
    }

    override fun stop() {
        ktor.stop()
        cloudflare.stop()

        _isRunning.update { false }
    }
}