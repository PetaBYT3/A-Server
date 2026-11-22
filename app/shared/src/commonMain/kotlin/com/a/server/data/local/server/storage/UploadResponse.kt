package com.a.server.data.local.server.storage

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UploadResponse(
    val success: Boolean,
    @SerialName("file_name")
    val fileName: String,
    val size: Long = 0L
)
