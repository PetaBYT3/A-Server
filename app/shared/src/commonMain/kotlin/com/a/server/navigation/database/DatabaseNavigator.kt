package com.a.server.navigation.database

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import org.koin.core.annotation.Single

@Single
class DatabaseNavigator {
    val backStack: SnapshotStateList<DatabaseKey> = mutableStateListOf(DatabaseKey.ListScreen)

    fun navigateTo(route: DatabaseKey) {
        backStack.add(route)
    }

    fun navigateToDetail(route: DatabaseKey) {
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