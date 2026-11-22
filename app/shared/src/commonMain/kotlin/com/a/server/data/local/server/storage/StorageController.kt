package com.a.server.data.local.server.storage

import com.a.server.data.local.preferences.SettingsApi
import io.ktor.http.ContentDisposition
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.PartData
import io.ktor.http.content.forEachPart
import io.ktor.server.application.Application
import io.ktor.server.request.receive
import io.ktor.server.request.receiveMultipart
import io.ktor.server.response.header
import io.ktor.server.response.respond
import io.ktor.server.response.respondFile
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import kotlinx.coroutines.flow.first
import java.io.File

fun Application.storageController(settingsApi: SettingsApi) {
    routing {
        route("/storage") {
            post("/download") {
                val request = call.receive<DownloadRequest>()
                val rootPath = settingsApi.storagePath.first()
                val targetDownloadFile = File(rootPath, request.downloadPath)

                if (request.downloadPath.isBlank()) {
                    call.respond(
                        status = HttpStatusCode.BadRequest,
                        message = DownloadResponse(
                            success = false,
                            message = "Invalid Download Request"
                        )
                    )
                    return@post
                }

                if (!targetDownloadFile.path.startsWith(rootPath)) {
                    call.respond(
                        status = HttpStatusCode.Forbidden,
                        message = DownloadResponse(
                            success = false,
                            message = "Access Denied"
                        )
                    )
                    return@post
                }

                if (targetDownloadFile.exists() && targetDownloadFile.isFile) {
                    call.response.header(
                        name = HttpHeaders.ContentDisposition,
                        value = ContentDisposition.Attachment.withParameter(
                            key = ContentDisposition.Parameters.FileName,
                            value = targetDownloadFile.name
                        ).toString()
                    )
                    call.respondFile(targetDownloadFile)
                } else {
                    call.respond(
                        status = HttpStatusCode.NotFound,
                        message = DownloadResponse(
                            success = false,
                            message = "File Doesn't Exist"
                        )
                    )
                }
            }

            post("/upload") {
                val multiPart = call.receiveMultipart()
                val subFolder = call.request.queryParameters["folder"].orEmpty()
                val rootPath = settingsApi.storagePath.first()

                var uploadedFileName = ""
                var totalBytesWritten = 0L

                multiPart.forEachPart { partData ->
                    when (partData) {
                        is PartData.FileItem -> {

                        }
                        else -> Unit
                    }
                }
            }

            post("/delete") {
                val request = call.receive<DeleteRequest>()
                val rootPath = settingsApi.storagePath.first()
                val targetDeleteFile = File(rootPath, request.deletePath)

                if (request.deletePath.isBlank()) {
                    call.respond(
                        status = HttpStatusCode.BadRequest,
                        message = DeleteResponse(
                            success = false,
                            message = "Invalid Delete Request"
                        )
                    )
                    return@post
                }

                if (!targetDeleteFile.path.startsWith(rootPath)) {
                    call.respond(
                        status = HttpStatusCode.Forbidden,
                        message = DeleteResponse(
                            success = false,
                            message = "Access Denied"
                        )
                    )
                    return@post
                }

                if (targetDeleteFile.exists() && targetDeleteFile.isFile) {
                    targetDeleteFile.delete()

                    call.respond(
                        status = HttpStatusCode.OK,
                        message = DeleteResponse(
                            success = true,
                            message = "File Deleted"
                        )
                    )
                } else {
                    call.respond(
                        status = HttpStatusCode.NotFound,
                        message = DeleteResponse(
                            success = false,
                            message = "File Doesn't Exist"
                        )
                    )
                }
            }
        }
    }
}