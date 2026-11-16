package com.a.server.presentation.database

import androidx.compose.material.icons.Icons
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
    val lastIsDatabaseExecuteSql = navigationState.backStack.last()::class == NavigationKey.DatabaseExecuteSql::class
    IconToggleButton(
        checked = lastIsDatabaseExecuteSql,
        onCheckedChange = {
            if (!lastIsDatabaseExecuteSql) {
                navigationState.navigateTo(NavigationKey.DatabaseExecuteSql)
            }
        },
        content = { Icon(Icons.Rounded.Terminal, null) }
    )

    val lastIsDatabaseScheme = true
    Icon
}