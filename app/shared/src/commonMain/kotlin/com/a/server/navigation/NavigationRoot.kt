@file:OptIn(ExperimentalMaterial3AdaptiveApi::class)

package com.a.server.navigation

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.WideNavigationRailDefaults
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.util.fastForEach
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.window.core.layout.WindowWidthSizeClass
import aserver.app.shared.generated.resources.Res
import aserver.app.shared.generated.resources.auth_select
import aserver.app.shared.generated.resources.table_select
import com.a.server.navigation.scene.ListDetailScene
import com.a.server.navigation.scene.rememberListDetailSceneStrategy
import com.a.server.presentation.authentication.detail.AuthenticationDetailScreen
import com.a.server.presentation.authentication.list.AuthenticationListScreen
import com.a.server.presentation.dashboard.main.DashboardMainScreen
import com.a.server.presentation.database.detail.DatabaseDetailScreen
import com.a.server.presentation.database.list.DatabaseListScreen
import com.a.server.presentation.ui.DetailPlaceholder
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

@Composable
fun NavigationRoot(
    navigationState: NavigationState = koinInject()
) {
    val adaptiveInfo = currentWindowAdaptiveInfoV2().windowSizeClass
    val windowSize = adaptiveInfo.windowWidthSizeClass
    val snackBarHostState = remember { SnackbarHostState() }
    val topLevel by navigationState.topLevel.collectAsStateWithLifecycle()

    NavigationSuiteScaffold(
        layoutType = when (windowSize) {
            WindowWidthSizeClass.COMPACT -> NavigationSuiteType.NavigationBar
            else -> NavigationSuiteType.WideNavigationRailCollapsed
        },
        navigationSuiteColors = NavigationSuiteDefaults.colors(
            navigationRailContainerColor = MaterialTheme.colorScheme.surfaceContainer,
            wideNavigationRailColors = WideNavigationRailDefaults.colors(
                containerColor = MaterialTheme.colorScheme.surfaceContainer
            )
        ),
        navigationSuiteItems = {
            TopLevelDestinations.entries.fastForEach { topLevelDestinations ->
                item(
                    selected = topLevel == topLevelDestinations,
                    onClick = {
                        navigationState.navigateTo(topLevelDestinations.key)
                    },
                    icon = {
                        Icon(
                            imageVector = topLevelDestinations.icon.asIcon(),
                            contentDescription = topLevelDestinations.label.asString()
                        )
                    },
                    label = {}
                )
            }
        },
        content = {
            Scaffold(
                topBar = {
                    TopAppBar(
                        navigationIcon = {
                            val validStack = navigationState.backStack.filter { navigationKey ->
                                navigationKey != NavigationKey.DashboardMain
                            }
                            AnimatedVisibility(validStack.size > 1) {
                                IconButton(
                                    onClick = { navigationState.navigateBack() },
                                    content = { Icon(Icons.Rounded.ArrowBackIosNew, null) }
                                )
                            }
                        },
                        title = {
                            AnimatedContent(topLevel) { targetState ->
                                Text(text = targetState.label.asString())
                            }
                        }
                    )
                },
                content = { innerPadding ->
                    NavDisplay(
                        modifier = Modifier
                            .padding(innerPadding),
                        backStack = navigationState.backStack,
                        onBack = { navigationState.navigateBack() },
                        sceneStrategies = listOf(
                            rememberListDetailSceneStrategy()
                        ),
                        entryDecorators = listOf(
                            rememberSaveableStateHolderNavEntryDecorator(),
                            rememberViewModelStoreNavEntryDecorator()
                        ),
                        entryProvider = entryProvider {
                            //Dashboard
                            entry<NavigationKey.DashboardMain>(
                                content = { DashboardMainScreen() }
                            )

                            //Authentication
                            entry<NavigationKey.AuthenticationList>(
                                metadata = ListDetailScene.listPane(
                                    placeholder = {
                                        DetailPlaceholder(
                                            message = stringResource(Res.string.auth_select)
                                        )
                                    }
                                ),
                                content = { AuthenticationListScreen() }
                            )
                            entry<NavigationKey.AuthenticationDetail>(
                                metadata = ListDetailScene.detailPane(),
                                content = { AuthenticationDetailScreen(it.authId) }
                            )

                            //Database
                            entry<NavigationKey.DatabaseList>(
                                metadata = ListDetailScene.listPane(
                                    placeholder = {
                                        DetailPlaceholder(
                                            message = stringResource(Res.string.table_select)
                                        )
                                    }
                                ),
                                content = { DatabaseListScreen() }
                            )
                            entry<NavigationKey.DatabaseDetail>(
                                metadata = ListDetailScene.detailPane(),
                                content = { DatabaseDetailScreen(it.tableName) }
                            )

                            //Storage
                            entry<NavigationKey.StorageMain>(
                                content = {}
                            )
                        },
                        transitionSpec = {
                            fadeIn() togetherWith fadeOut()
                        },
                        popTransitionSpec = {
                            fadeIn() togetherWith fadeOut()
                        },
                        predictivePopTransitionSpec = { progress ->
                            fadeIn() togetherWith fadeOut()
                        },
                    )
                },
                snackbarHost = {
                    SnackbarHost(hostState = snackBarHostState)
                }
            )
        }
    )
}