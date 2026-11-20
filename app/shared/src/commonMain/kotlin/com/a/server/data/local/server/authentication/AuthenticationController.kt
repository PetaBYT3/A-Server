package com.a.server.data.local.server.authentication

import at.favre.lib.crypto.bcrypt.BCrypt
import com.a.server.data.local.room.ApplicationDatabase
import com.a.server.data.local.room.authentication.AuthenticationEntity
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import kotlinx.coroutines.flow.first
import ulid.ULID
import kotlin.time.Clock

fun Application.authenticationController(applicationDatabase: ApplicationDatabase) {
    val authenticationDao = applicationDatabase.authenticationDao

    routing {
        route("/authentication") {
            post("/signin") {
                val request = call.receive<AuthenticationRequest>()
                val currentAuth = authenticationDao.getSingleByEmail(request.email).first()

                if (currentAuth == null) {
                    call.respond(
                        status = HttpStatusCode.Unauthorized,
                        message = AuthenticationResponse(
                            success = false,
                            message = "Email or password incorrect"
                        )
                    )
                    return@post
                }

                val isPasswordCorrect = BCrypt.verifyer().verify(
                    request.password.toCharArray(),
                    currentAuth.password
                ).verified

                if (!isPasswordCorrect) {
                    call.respond(
                        status = HttpStatusCode.Unauthorized,
                        message = AuthenticationResponse(
                            success = false,
                            message = "Email or password incorrect"
                        )
                    )
                    return@post
                }

                call.respond(
                    status = HttpStatusCode.OK,
                    message = AuthenticationResponse(true, "Authentication Successful")
                )
            }

            post("/signup") {
                val request = call.receive<AuthenticationRequest>()
                val currentAuth = authenticationDao.getSingleByEmail(request.email).first()

                if (currentAuth?.email != null) {
                    call.respond(
                        status = HttpStatusCode.Conflict,
                        message = AuthenticationResponse(
                            success = false,
                            message = "Email Already Used"
                        )
                    )
                    return@post
                }

                val encryptedPassword = BCrypt
                    .withDefaults()
                    .hashToString(12, request.password.toCharArray())

                val newAuth = AuthenticationEntity(
                    id = ULID.randomULID(),
                    email = request.email,
                    password = encryptedPassword,
                    createdAt = Clock.System.now().toEpochMilliseconds()
                )
                authenticationDao.upsert(newAuth)

                call.respond(
                    status = HttpStatusCode.Created,
                    message = AuthenticationResponse(true, "Account Created")
                )
            }
        }
    }
}