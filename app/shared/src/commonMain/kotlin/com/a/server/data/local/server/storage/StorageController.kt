package com.a.server.data.local.server.storage

import com.a.server.data.local.preferences.SettingsApi
import io.ktor.server.application.Application
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import io.ktor.server.routing.routing

fun Application.storageController(settingsApi: SettingsApi) {
    routing {
        route("/storage") {
            post("/download") {

            }

            post("/upload") {

            }

            post("/delete") {

            }
        }
    }
}