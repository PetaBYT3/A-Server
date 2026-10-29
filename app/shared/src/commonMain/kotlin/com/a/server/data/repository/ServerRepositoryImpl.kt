package com.a.server.data.repository

import arrow.core.Either
import com.a.server.core.manager.JobManager
import com.a.server.core.manager.SystemManager
import com.a.server.data.local.preferences.SettingsApi
import com.a.server.data.local.server.ServerEngineApi
import com.a.server.domain.model.server.ServerModel
import com.a.server.domain.repository.ServerRepository
import io.ktor.server.engine.EmbeddedServer
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
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
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import org.koin.core.annotation.Single
import kotlin.time.Duration.Companion.seconds

@Single
class ServerRepositoryImpl(
    private val settingsApi: SettingsApi,
    private val serverEngineApi: ServerEngineApi
): ServerRepository {
    private companion object {
        const val SERVER_JOB = "serverJob"
        const val COUNT_JOB = "countJob"
    }

    private val repositoryScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val jobs = JobManager(repositoryScope)

    private var serverInstance: EmbeddedServer<*, *>? = null

    private val _serverState = MutableStateFlow(ServerModel())
    override val serverState: StateFlow<ServerModel> = _serverState.asStateFlow()

    override fun toggle(): Flow<Either<String, String>> {
        return callbackFlow<Either<String, String>> {
            if (serverInstance != null) {
                destroy()
                send(Either.Right("Server Stopped"))
                close()
            } else {
                serverInstance = serverEngineApi.getEngine(
                    host = settingsApi.serverHost.first(),
                    port = settingsApi.serverPort.first()
                ) {
                    routing {
                        get("/") {
                            val platformInfo = SystemManager().getInfo()
                            call.respondText("Hello from Ktor Server running on $platformInfo")
                        }
                    }
                }

                jobs.launchJob(SERVER_JOB) {
                    serverInstance?.start(false)
                }
                _serverState.update { currentState ->
                    currentState.copy(isRunning = true)
                }

                startCount()
                send(Either.Right("Server Started"))
                close()
            }

            awaitClose()
        }.catch { throwable ->
            destroy()
            emit(Either.Left(throwable.message ?: "Unknown Error"))
        }.flowOn(Dispatchers.IO)
    }

    private fun startCount() {
        val startTime = System.currentTimeMillis()

        jobs.launchJob(COUNT_JOB) {
            while (isActive) {
                _serverState.update { currentState ->
                    currentState.copy(uptime = System.currentTimeMillis() - startTime)
                }
                delay(1.seconds)
            }
        }
    }

    private suspend fun destroy() {
        serverInstance?.stop(gracePeriodMillis = 1_000, timeoutMillis = 2_000)
        serverInstance = null

        jobs.stopJob(SERVER_JOB)
        jobs.stopJob(COUNT_JOB)

        _serverState.update { currentState ->
            currentState.copy(isRunning = false)
        }
    }
}