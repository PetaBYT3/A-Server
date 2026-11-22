package com.a.server.data.local.server.storage

import kotlinx.serialization.Serializable

@Serializable
data class DeleteResponse(
    val success: Boolean,
    val message: String
)