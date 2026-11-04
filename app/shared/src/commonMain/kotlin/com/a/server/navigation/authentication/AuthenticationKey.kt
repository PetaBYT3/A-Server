package com.a.server.navigation.authentication

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface AuthenticationKey: NavKey {
    @Serializable
    data object ListScreen: AuthenticationKey

    @Serializable
    data class DetailScreen(val authId: String): AuthenticationKey
}