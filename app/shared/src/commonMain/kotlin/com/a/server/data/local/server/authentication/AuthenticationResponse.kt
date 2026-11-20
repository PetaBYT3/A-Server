package com.a.server.data.local.server.authentication

import kotlinx.serialization.Serializable

@Serializable
data class AuthenticationResponse(
    val success: Boolean,
    val message: String
)