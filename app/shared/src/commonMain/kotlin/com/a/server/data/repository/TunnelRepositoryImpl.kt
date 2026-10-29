@file:Suppress("BlockingMethodInNonBlockingContext")

package com.a.server.data.repository

import arrow.core.Either
import com.a.server.core.manager.JobManager
import com.a.server.data.local.preferences.SettingsApi
import com.a.server.data.local.platform.BinaryManagerApi
import com.a.server.data.util.catchAndDispatch
import com.a.server.data.util.urlRegex
import com.a.server.domain.model.tunnel.TunnelModel
import com.a.server.domain.repository.TunnelRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
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
    private companion object {
        const val TUNNEL_JOB = "tunnelJob"
        const val COUNT_JOB = "countJob"
    }

    private val repositoryScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private val jobs = JobManager(repositoryScope)

    private var tunnelProcess: Process? = null

    private val _tunnelState = MutableStateFlow(TunnelModel())
    override val tunnelState: StateFlow<TunnelModel> = _tunnelState.asStateFlow()

    override fun toggle(): Flow<Either<String, String>> {
        return callbackFlow<Either<String, String>> {
            if (tunnelProcess != null) {
                destroy()
                send(Either.Right("Tunnel Stopped"))
                close()
            } else {
                val binaryFile = binaryManagerApi.getCloudflarePlatformBinaryFile()
                val port = settingsApi.serverPort.first()
                val command = ProcessBuilder(
                    binaryFile.absolutePath,
                    "tunnel",
                    "--url",
                    "http://localhost:$port"
                )

                binaryFile.setExecutable(true, false)
                tunnelProcess = command.redirectErrorStream(true).start()

                jobs.launchJob(TUNNEL_JOB) {
                    val scanner = Scanner(tunnelProcess!!.inputStream)
                    while (scanner.hasNextLine() && tunnelProcess != null) {
                        val line = scanner.nextLine()
                        val matchResult = urlRegex.find(line)

                        if (matchResult != null) {
                            _tunnelState.update { currentState ->
                                currentState.copy(
                                    isRunning = true,
                                    publicUrl = matchResult.value
                                )
                            }
                            startCount()
                            send(Either.Right("Tunnel Started"))
                            close()
                        }
                    }
                }
            }

            awaitClose()
        }.catch { throwable ->
            destroy()
            emit(Either.Left(throwable.message ?: "Unknown Error"))
        }
    }

    private fun startCount() {
        val startTime = System.currentTimeMillis()

        jobs.launchJob(COUNT_JOB) {
            while (isActive) {
                _tunnelState.update { currentState ->
                    currentState.copy(uptime = System.currentTimeMillis() - startTime)
                }
                delay(1.seconds)
            }
        }
    }

    private suspend fun destroy() {
        tunnelProcess?.destroy()
        tunnelProcess = null

        jobs.stopJob(TUNNEL_JOB)
        jobs.stopJob(COUNT_JOB)

        _tunnelState.update { currentState ->
            currentState.copy(isRunning = false, publicUrl = null)
        }
    }
}