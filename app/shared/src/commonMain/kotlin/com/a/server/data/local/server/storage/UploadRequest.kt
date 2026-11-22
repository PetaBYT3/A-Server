package com.a.server.data.local.server.storage

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UploadRequest(
    @SerialName("target_path")
    val targetPath: String,
    @SerialName("file_base64")
    val fileBase64: String
)