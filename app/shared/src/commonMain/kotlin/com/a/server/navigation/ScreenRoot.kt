@file:OptIn(ExperimentalSerializationApi::class, ExperimentalMaterial3AdaptiveApi::class)

package com.a.server.navigation

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
import aserver.app.shared.generated.resources.Res
import aserver.app.shared.generated.resources.auth_select
import aserver.app.shared.generated.resources.table_select
import com.a.server.presentation.auth.AuthenticationScreen
import com.a.server.presentation.authdetail.AuthDetailScreen
import com.a.server.presentation.dashboard.DashboardScreen
import com.a.server.presentation.rowupsert.RowUpsertScreen
import com.a.server.presentation.table.TableScreen
import com.a.server.presentation.tabledetail.TableDetailScreen
import com.a.server.presentation.tablesql.TableSqlScreen
import com.a.server.presentation.tableupsert.TableUpsertScreen
import com.a.server.presentation.ui.DetailPlaceholder
import kotlinx.serialization.ExperimentalSerializationApi
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

@Composable
fun ScreenRoot() {
    val navigator: Navigator = koinInject()

    val windowAdaptiveInfo = currentWindowAdaptiveInfoV2()
    val directive = remember(windowAdaptiveInfo) {
        calculatePaneScaffoldDirective(windowAdaptiveInfo).copy(
            horizontalPartitionSpacerSize = 10.dp,
            defaultPanePreferredWidth = 500.dp
        )
    }
    val listDetailStrategy = rememberListDetailSceneStrategy<Any>(
        directive = directive
    )

    val transitionDuration = 250
    NavDisplay(
        backStack = navigator.backStack,
        onBack = { navigator.navigateBack() },
        sceneStrategies = listOf(listDetailStrategy),
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        entryProvider = entryProvider {
            entry<Route.DashboardScreen>(
                content = { DashboardScreen(navigator) }
            )
            entry<Route.AuthScreen>(
                metadata = ListDetailSceneStrategy.listPane(
                    detailPlaceholder = {
                        DetailPlaceholder(
                            message = stringResource(Res.string.auth_select)
                        )
                    }
                ),
                content = { AuthenticationScreen() }
            )
            entry<Route.AuthDetailScreen>(
                metadata = ListDetailSceneStrategy.detailPane(),
                content = { AuthDetailScreen(it.authId) }
            )
            entry<Route.TableScreen>(
                metadata = ListDetailSceneStrategy.listPane(
                    detailPlaceholder = {
                        DetailPlaceholder(
                            message = stringResource(Res.string.table_select)
                        )
                    }
                ),
                content = { TableScreen() }
            )
            entry<Route.TableDetailScreen>(
                metadata = ListDetailSceneStrategy.detailPane(),
                content = { TableDetailScreen(it.name) }
            )
            entry<Route.TableUpsertScreen>(
                metadata = ListDetailSceneStrategy.detailPane(),
                content = { TableUpsertScreen(it.name) }
            )
            entry<Route.TableSqlScreen>(
                metadata = ListDetailSceneStrategy.extraPane(),
                content = { TableSqlScreen() }
            )
            entry<Route.RowUpsertScreen>(
                metadata = ListDetailSceneStrategy.extraPane(),
                content = { RowUpsertScreen(it.tableName, it.rowId) }
            )
        },
//        transitionSpec = {
//            slideInHorizontally(
//                initialOffsetX = { it },
//                animationSpec = tween(transitionDuration, easing = FastOutSlowInEasing)
//            ) togetherWith slideOutHorizontally(
//                targetOffsetX = { -it / 3 },
//                animationSpec = tween(transitionDuration, easing = FastOutSlowInEasing)
//            )
//        },
//        popTransitionSpec = {
//            slideInHorizontally(
//                initialOffsetX = { -it / 3 },
//                animationSpec = tween(transitionDuration, easing = FastOutSlowInEasing)
//            ) togetherWith slideOutHorizontally(
//                targetOffsetX = { it },
//                animationSpec = tween(transitionDuration, easing = FastOutSlowInEasing)
//            )
//        },
//        predictivePopTransitionSpec = {
//            slideInHorizontally(
//                initialOffsetX = { -it / 3 },
//                animationSpec = tween(transitionDuration, easing = FastOutSlowInEasing)
//            ) togetherWith slideOutHorizontally(
//                targetOffsetX = { it },
//                animationSpec = tween(transitionDuration, easing = FastOutSlowInEasing)
//            )
//        }
    )
}