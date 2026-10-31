package com.a.server.data.local.server

import com.a.server.data.local.room.ApplicationDatabase
import com.a.server.data.local.room.AuthenticationEntity
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import kotlinx.serialization.Serializable
import ulid.ULID
import kotlin.time.Clock

fun Application.authenticationController(applicationDatabase: ApplicationDatabase) {
    @Serializable
    data class AuthenticationRequest(
        val email: String,
        val password: String
    )

    @Serializable
    data class AuthenticationResponse(
        val success: Boolean,
        val message: String
    )

    val authenticationDao = applicationDatabase.authenticationDao

    routing {
        route("/authentication") {
            post("/signin") {
                val request = call.receive<AuthenticationRequest>()
                val isAuthenticateFound = authenticationDao.getSingleByEmailAndPassword(
                    email = request.email,
                    password = request.password
                ) != null

                if (isAuthenticateFound) {
                    call.respond(
                        status = HttpStatusCode.OK,
                        message = AuthenticationResponse(true, "Authentication Successful")
                    )
                } else {
                    call.respond(
                        status = HttpStatusCode.Unauthorized,
                        message = AuthenticationResponse(false, "Authentication Failed")
                    )
                }
            }

            post("/signup") {
                val request = call.receive<AuthenticationRequest>()
                val newAuthentication = AuthenticationEntity(
                    id = ULID.randomULID(),
                    email = request.email,
                    password = request.password,
                    createdAt = Clock.System.now().toEpochMilliseconds()
                )
                authenticationDao.upsert(newAuthentication)

                call.respond(
                    status = HttpStatusCode.Created,
                    message = AuthenticationResponse(true, "Account Created")
                )
            }
        }
    }
}