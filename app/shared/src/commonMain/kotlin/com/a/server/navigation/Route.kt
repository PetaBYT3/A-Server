package com.a.server.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed class Route: NavKey {
    @Serializable
    object DashboardScreen: Route()

    @Serializable
    object AuthScreen: Route()

    @Serializable
    class AuthDetailScreen(val authId: String): Route()

    @Serializable
    object TableScreen: Route()

    @Serializable
    class TableDetailScreen(val tableId: String): Route()

    @Serializable
    class TableUpsertScreen(val tableId: String): Route()
}