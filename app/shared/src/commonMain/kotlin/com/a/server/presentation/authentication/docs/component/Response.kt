package com.a.server.presentation.authentication.docs.component

import kotlinx.serialization.Serializable

@Serializable
data class Response(
    val success: String,
    val message: String
)
