package com.a.server.data.server

import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.install
import io.ktor.server.cio.CIO
import io.ktor.server.cio.CIOApplicationEngine
import io.ktor.server.engine.EmbeddedServer
import io.ktor.server.engine.embeddedServer
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import kotlinx.serialization.json.Json
import org.koin.core.annotation.Single

@Single
class KtorImpl: Ktor {
    private var server: EmbeddedServer<CIOApplicationEngine, CIOApplicationEngine.Configuration>? = null

    override fun start() {
        if (server != null) {
            return
        }

        server = embeddedServer(CIO, 8080, "0.0.0.0") {
            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                })
            }

            routing {
                get("/hello") {
                    call.respondText("Hello from Ktor server hosted in android devices")
                }
            }
        }

        server?.start(false)
    }

    override fun stop() {
        server?.stop(1000, 2000)
        server = null
    }
}