package com.a.server.data.local.server.database

import com.a.server.data.local.sqlite.ServerDatabaseApi
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import io.ktor.server.routing.routing

fun Application.databaseController(serverDatabaseApi: ServerDatabaseApi) {
    routing {
        route("/database") {
            post("/get") {
                val request = call.receive<GetRequest>()
                val tableName = request.tableName

                val rows = serverDatabaseApi.getDynamicRows(tableName)

                call.respond(
                    status = HttpStatusCode.OK,
                    message = GetResponse(
                        success = true,
                        message = "Query Success",
                        data = rows
                    )
                )
            }

            post("/upsert") {

            }

            post("/delete") {

            }

            post("/execute") {

            }
        }
    }
}