package com.a.server.navigation.dashboard

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import org.koin.core.annotation.Single

@Single
class DashboardNavigator {
    val backStack: SnapshotStateList<DashboardKey> = mutableStateListOf(DashboardKey.MainScreen)

    fun navigateTo(key: DashboardKey) {
        backStack.add(key)
    }

    fun navigateBack() {
        if (backStack.size > 1) {
            backStack.removeLastOrNull()
        }
    }
}