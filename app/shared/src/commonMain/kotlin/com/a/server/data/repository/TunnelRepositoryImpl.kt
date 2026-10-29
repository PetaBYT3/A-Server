package com.a.server.data.repository

import arrow.core.Either
import com.a.server.data.local.preferences.SettingsApi
import com.a.server.data.mapper.TimeMapper
import com.a.server.data.server.CloudflareManager
import com.a.server.data.system.BinaryManagerApi
import com.a.server.data.util.catchAndDispatch
import com.a.server.data.util.urlRegex
import com.a.server.domain.model.TunnelModel
import com.a.server.domain.repository.TunnelRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.koin.core.annotation.Single
import java.util.Scanner
import kotlin.time.Duration.Companion.seconds

@Single
class TunnelRepositoryImpl(
    private val settingsApi: SettingsApi,
    private val binaryManagerApi: BinaryManagerApi
): TunnelRepository {
    private val tunnelRepositoryScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    private var process: Process? = null
    private var countJob: Job? = null

    private val _state = MutableStateFlow(TunnelModel())
    override val state: StateFlow<TunnelModel> = _state.asStateFlow()

    override fun start(): Flow<Either<String, String>> {
        return callbackFlow {
            if (process != null) {
                trySend(Either.Left("Tunnel is already running"))
                close()
                return@callbackFlow
            }

            val binaryFile = binaryManagerApi.getCloudflarePlatformBinaryFile()
            val port = settingsApi.serverPort.first()
            val command = ProcessBuilder(
                binaryFile.absolutePath,
                "tunnel",
                "--url",
                "http://localhost:$port"
            )

            binaryFile.setExecutable(true, false)
            process = command.redirectErrorStream(true).start()

            tunnelRepositoryScope.launch {
                val scanner = Scanner(process!!.inputStream)

                while (scanner.hasNextLine() && process != null) {
                    val line = scanner.nextLine()
                    val matchResult = urlRegex.find(line)

                    if (matchResult != null) {
                        _state.update { currentState ->
                            currentState.copy(
                                isRunning = true,
                                publicUrl = matchResult.value
                            )
                        }

                        startUptime()
                    }
                }

                trySend(Either.Right("Tunnel stopped"))
                close()
            }

            awaitClose()
        }.catchAndDispatch()
    }

    override fun stop() {
        process?.destroy()
        processExit()
    }

    private fun startUptime() {
        countJob?.cancel()
        val startTime = System.currentTimeMillis()

        countJob = tunnelRepositoryScope.launch {
            while (isActive) {
                _state.update { currentState ->
                    currentState.copy(uptime = System.currentTimeMillis() - startTime)
                }
                delay(1.seconds)
            }
        }
    }

    private fun stopUptime() {
        countJob?.cancel()
        countJob = null
    }

    private fun processExit() {
        process = null
        _state.update { currentState ->
            currentState.copy(isRunning = false, publicUrl = null)
        }
        stopUptime()
    }
}