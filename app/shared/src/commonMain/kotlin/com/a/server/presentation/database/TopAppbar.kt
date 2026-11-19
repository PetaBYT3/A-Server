package com.a.server.presentation.database

import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Schema
import androidx.compose.material.icons.rounded.Terminal
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.runtime.Composable
import com.a.server.navigation.NavigationKey
import com.a.server.navigation.NavigationState

@Composable
fun DatabaseAction(
    navigationState: NavigationState
) {
    Row {
        val lastIsDatabaseScheme = navigationState.backStack.last()::class ==
                NavigationKey.DatabaseScheme::class
        IconToggleButton(
            checked = lastIsDatabaseScheme,
            onCheckedChange = {
                if (!lastIsDatabaseScheme) {
                    navigationState.navigateToDetail(NavigationKey.DatabaseScheme)
                }
            },
            content = { Icon(Icons.Rounded.Schema, null) }
        )
        val lastIsDatabaseExecuteSql = navigationState.backStack.last()::class ==
                NavigationKey.DatabaseExecuteSql::class
        IconToggleButton(
            checked = lastIsDatabaseExecuteSql,
            onCheckedChange = {
                if (!lastIsDatabaseExecuteSql) {
                    navigationState.navigateToDetail(NavigationKey.DatabaseExecuteSql)
                }
            },
            content = { Icon(Icons.Rounded.Terminal, null) }
        )
    }
}