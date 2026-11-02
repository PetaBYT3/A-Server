package com.a.server.navigation

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import org.koin.core.annotation.Single

@Single
class Navigator {
    val backStack: SnapshotStateList<Any> = mutableStateListOf(Route.DashboardScreen)

    fun navigateTo(route: Route) {
        backStack.add(route)
    }

    fun navigateToDetailOrExtra(route: Route) {
        if (backStack.lastIndex::class == route::class) {
            backStack[backStack.lastIndex] = route
        } else {
            backStack.add(route)
        }
    }

    fun navigateBack() {
        backStack.removeLastOrNull()
    }
}