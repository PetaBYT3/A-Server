package com.a.server.data.local.server

import io.ktor.server.application.Application
import io.ktor.server.cio.CIOApplicationEngine
import io.ktor.server.engine.EmbeddedServer

interface ServerEngineApi {
    fun getEngine(
        port: Int,
        host: String,
        module: suspend Application.() -> Unit
    ): EmbeddedServer<CIOApplicationEngine, CIOApplicationEngine.Configuration>
}