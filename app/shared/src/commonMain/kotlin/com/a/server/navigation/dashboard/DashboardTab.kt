@file:OptIn(ExperimentalMaterial3AdaptiveApi::class)

package com.a.server.navigation.dashboard

import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2
import androidx.compose.material3.adaptive.layout.calculatePaneScaffoldDirective
import androidx.compose.material3.adaptive.navigation3.rememberListDetailSceneStrategy
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.a.server.presentation.dashboard.main.MainScreen
import org.koin.compose.koinInject

@Composable
fun DashboardTab(
    navigator: DashboardNavigator = koinInject()
) {
    val windowAdaptiveInfo = currentWindowAdaptiveInfoV2()

    NavDisplay(
        backStack = navigator.backStack,
        onBack = { navigator.navigateBack() },
        sceneStrategies = listOf(
            rememberListDetailSceneStrategy(
                directive = remember(windowAdaptiveInfo) {
                    calculatePaneScaffoldDirective(windowAdaptiveInfo).copy(
                        horizontalPartitionSpacerSize = 0.dp
                    )
                }
            )
        ),
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        entryProvider = entryProvider {
            entry<DashboardKey.MainScreen>(
                content = { MainScreen() }
            )
        }
    )
}