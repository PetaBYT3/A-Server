package com.a.server.data.local.server.storage

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DeleteRequest(
    @SerialName("delete_path")
    val deletePath: String
)
