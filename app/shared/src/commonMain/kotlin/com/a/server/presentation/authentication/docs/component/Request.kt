package com.a.server.presentation.authentication.docs.component

import kotlinx.serialization.Serializable

@Serializable
data class Request(
    val email: String,
    val password: String
)
