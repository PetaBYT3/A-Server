package com.a.server.presentation.authentication

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DocumentScanner
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.runtime.Composable
import com.a.server.navigation.NavigationKey
import com.a.server.navigation.NavigationState

@Composable
fun AuthenticationAction(
    navigationState: NavigationState
) {
    IconToggleButton(
        checked = navigationState.backStack.last()::class == NavigationKey.AuthenticationDocs::class,
        onCheckedChange = { navigationState.navigateToDetail(NavigationKey.AuthenticationDocs) },
        content = { Icon(Icons.Rounded.DocumentScanner, null) }
    )
}