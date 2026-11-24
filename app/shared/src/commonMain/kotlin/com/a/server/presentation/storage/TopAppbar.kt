package com.a.server.presentation.storage

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DocumentScanner
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.runtime.Composable
import com.a.server.navigation.NavigationKey
import com.a.server.navigation.NavigationState

@Composable
fun StorageAction(
    navigationState: NavigationState
) {
    IconToggleButton(
        checked = false,
        onCheckedChange = {  },
        content = { Icon(Icons.Rounded.DocumentScanner, null) }
    )
}