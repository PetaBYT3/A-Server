package com.a.server.data.local.server.storage

import kotlinx.serialization.Serializable

@Serializable
data class DownloadResponse(
    val success: Boolean,
    val message: String,
)
