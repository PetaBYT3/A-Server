@file:OptIn(ExperimentalMaterial3AdaptiveApi::class, ExperimentalMaterial3AdaptiveApi::class)

package com.a.server.navigation.database

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2
import androidx.compose.material3.adaptive.layout.calculatePaneScaffoldDirective
import androidx.compose.material3.adaptive.navigation3.ListDetailSceneStrategy
import androidx.compose.material3.adaptive.navigation3.rememberListDetailSceneStrategy
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.a.server.presentation.database.detail.DetailScreen
import com.a.server.presentation.database.list.ListScreen
import com.a.server.presentation.database.upsertrow.UpsertRowScreen
import com.a.server.presentation.database.upserttable.UpsertTableScreen
import org.koin.compose.koinInject

@Composable
fun DatabaseTab(
    navigator: DatabaseNavigator = koinInject()
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
        transitionSpec = {
            fadeIn(animationSpec = tween(300)) + slideInHorizontally { it / 4 } togetherWith
                    fadeOut(animationSpec = tween(300)) + slideOutHorizontally { -it / 4 }
        },
        entryProvider = entryProvider {
            entry<DatabaseKey.ListScreen>(
                metadata = ListDetailSceneStrategy.listPane(
                    detailPlaceholder = {}
                ),
                content = { ListScreen() }
            )
            entry<DatabaseKey.DetailScreen>(
                metadata = ListDetailSceneStrategy.detailPane(),
                content = { DetailScreen(it.tableName) }
            )
            entry<DatabaseKey.UpsertTable>(
                metadata = ListDetailSceneStrategy.detailPane(),
                content = { UpsertTableScreen(it.tableName) }
            )
            entry<DatabaseKey.UpsertRow>(
                metadata = ListDetailSceneStrategy.extraPane(),
                content = { UpsertRowScreen(it.tableName, it.rowId) }
            )
        }
    )
}