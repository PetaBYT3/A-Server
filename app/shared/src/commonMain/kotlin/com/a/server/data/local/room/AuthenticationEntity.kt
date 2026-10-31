package com.a.server.data.local.room

import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "authentication")
data class AuthenticationEntity(
    @PrimaryKey
    val id: String,
    val email: String,
    val password: String,
    val createdAt: Long
)
