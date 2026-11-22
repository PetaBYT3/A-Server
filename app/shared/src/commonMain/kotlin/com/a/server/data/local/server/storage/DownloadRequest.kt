package com.a.server.data.local.server.storage

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DownloadRequest(
    @SerialName("download_path")
    val downloadPath: String
)
