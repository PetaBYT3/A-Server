package com.a.server.presentation.dashboard

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import aserver.app.shared.generated.resources.Res
import aserver.app.shared.generated.resources.dashboard
import com.a.server.navigation.Navigator
import com.a.server.presentation.dashboard.layout.DashboardCompact
import com.a.server.presentation.dashboard.layout.DashboardExpanded
import com.a.server.presentation.dashboard.layout.DashboardMedium
import com.a.server.presentation.ui.TransparentLargeTopAppBar
import com.a.server.presentation.ui.WindowType
import com.a.server.presentation.ui.rememberWindowSize
import com.a.server.presentation.util.EffectLauncher
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun DashboardScreen(
    navigator: Navigator,
    viewModel: DashboardViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val windowSize = rememberWindowSize()
    val snackBarHostState = remember { SnackbarHostState() }

    EffectLauncher(
        snackBarHostState = snackBarHostState,
        screenEffect = viewModel.effect
    )

    val topAppBarState = rememberTopAppBarState()
    val scrollBehaviour = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(
        state = topAppBarState
    )

    Scaffold(
        modifier = Modifier
            .nestedScroll(scrollBehaviour.nestedScrollConnection),
        topBar = {
            TransparentLargeTopAppBar(
                scrollBehavior = scrollBehaviour,
                title = stringResource(Res.string.dashboard)
            )
        },
        content = { innerPadding ->
            when (windowSize) {
                WindowType.Compact -> {
                    DashboardCompact(
                        modifier = Modifier
                            .padding(innerPadding),
                        navigator = navigator,
                        state = state,
                        onAction = viewModel::onAction
                    )
                }
                WindowType.Medium -> {
                    DashboardMedium(
                        modifier = Modifier
                            .padding(innerPadding),
                        navigator = navigator,
                        state = state,
                        onAction = viewModel::onAction
                    )
                }
                WindowType.Expanded -> {
                    DashboardExpanded(
                        modifier = Modifier
                            .padding(innerPadding),
                        navigator = navigator,
                        state = state,
                        onAction = viewModel::onAction
                    )
                }
            }
        },
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState)
        }
    )
}