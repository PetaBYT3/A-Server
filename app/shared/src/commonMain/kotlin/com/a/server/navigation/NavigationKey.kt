package com.a.server.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface NavigationKey: NavKey {
    @Serializable
    data object DashboardMain: NavigationKey

    @Serializable
    data object AuthenticationList: NavigationKey

    @Serializable
    data class AuthenticationDetail(val authId: String): NavigationKey

    @Serializable
    data object DatabaseList: NavigationKey

    @Serializable
    data class DatabaseDetail(val tableName: String): NavigationKey

    @Serializable
    data object StorageMain: NavigationKey
}