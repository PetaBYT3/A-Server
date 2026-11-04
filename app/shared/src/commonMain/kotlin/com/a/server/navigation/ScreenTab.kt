package com.a.server.navigation

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.WideNavigationRailDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.util.fastForEach
import com.a.server.navigation.authentication.AuthenticationTab
import com.a.server.navigation.dashboard.DashboardTab
import com.a.server.navigation.database.DatabaseTab
import com.a.server.presentation.ui.WindowType
import com.a.server.presentation.ui.rememberWindowSize

@Composable
fun ScreenTab() {
    var currentRoute by remember {
        mutableStateOf(ScreenKey.DASHBOARD)
    }
    val windowSize = rememberWindowSize()

    NavigationSuiteScaffold(
        layoutType = when (windowSize) {
            WindowType.Compact -> NavigationSuiteType.NavigationBar
            else -> NavigationSuiteType.WideNavigationRailCollapsed
        },
        navigationSuiteColors = NavigationSuiteDefaults.colors(
            navigationRailContainerColor = MaterialTheme.colorScheme.surfaceContainer,
            wideNavigationRailColors = WideNavigationRailDefaults.colors(
                containerColor = MaterialTheme.colorScheme.surfaceContainer
            )
        ),
        navigationSuiteItems = {
            ScreenKey.entries.fastForEach { topLevelRoute ->
                item(
                    selected = currentRoute == topLevelRoute,
                    onClick = { currentRoute = topLevelRoute },
                    icon = {
                        Icon(
                            imageVector = topLevelRoute.icon.asIcon(),
                            contentDescription = null
                        )
                    },
                    label = {}
                )
            }
        },
        content = {
            when (currentRoute) {
                ScreenKey.DASHBOARD -> DashboardTab()
                ScreenKey.AUTHENTICATION -> AuthenticationTab()
                ScreenKey.DATABASE -> DatabaseTab()
                ScreenKey.STORAGE -> {}
            }
        }
    )
}