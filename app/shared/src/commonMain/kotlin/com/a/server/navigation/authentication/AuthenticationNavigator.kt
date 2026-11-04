package com.a.server.navigation.authentication

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import org.koin.core.annotation.Single

@Single
class AuthenticationNavigator {
    val backStack: SnapshotStateList<AuthenticationKey> = mutableStateListOf(AuthenticationKey.ListScreen)

    fun navigateTo(route: AuthenticationKey) {
        backStack.add(route)
    }

    fun navigateToDetail(route: AuthenticationKey) {
        if (backStack.lastIndex::class == route::class) {
            backStack[backStack.lastIndex] = route
        } else {
            backStack.add(route)
        }
    }

    fun navigateBack() {
        if (backStack.size > 1) {
            backStack.removeLastOrNull()
        }
    }
}