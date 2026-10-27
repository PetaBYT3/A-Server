package com.a.server.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed class Route: NavKey {
    @Serializable
    object DashboardScreen: Route()
}