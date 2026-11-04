package com.a.server.navigation.dashboard

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface DashboardKey: NavKey {
    @Serializable
    data object MainScreen: DashboardKey
}