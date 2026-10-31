package com.a.server.domain.model.authentication

data class AuthenticationModel(
    val id: String,
    val email: String,
    val password: String,
    val createdAt: Long
)
