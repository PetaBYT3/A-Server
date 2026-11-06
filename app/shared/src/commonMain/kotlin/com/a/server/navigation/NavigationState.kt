package com.a.server.navigation

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.koin.core.annotation.Single

@Single
class NavigationState {
    private val _topLevel = MutableStateFlow(TopLevelDestinations.DASHBOARD)
    val topLevel = _topLevel.asStateFlow()

    val backStack: SnapshotStateList<NavigationKey> = mutableStateListOf(
        NavigationKey.DashboardMain
    )

    fun navigateTo(navigationKey: NavigationKey) {
        when (navigationKey) {
            NavigationKey.DashboardMain -> {
                backStack.clear()
                backStack.add(navigationKey)
                updateTopLevel(navigationKey)
            }
            NavigationKey.AuthenticationList,
            NavigationKey.DatabaseList,
            NavigationKey.StorageMain -> {
                backStack.clear()
                backStack.addAll(listOf(NavigationKey.DashboardMain, navigationKey))
                updateTopLevel(navigationKey)
            }
            else -> {
                backStack.add(navigationKey)
            }
        }
    }

    fun navigateBack() {
        if (backStack.size > 1) {
            backStack.removeLastOrNull()
        }

        backStack.lastOrNull()?.let { lastItem ->
            updateTopLevel(lastItem)
        }
    }

    private fun updateTopLevel(navigationKey: NavigationKey) {
        TopLevelDestinations.entries.find { it.key == navigationKey }?.let { topLevelDestinations ->
            _topLevel.update { topLevelDestinations }
        }
    }
}