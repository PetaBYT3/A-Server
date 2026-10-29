package com.a.server.presentation.dashboard

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.a.server.navigation.Navigator
import com.a.server.presentation.dashboard.layout.DashboardCompact
import com.a.server.presentation.dashboard.layout.DashboardExpanded
import com.a.server.presentation.dashboard.layout.DashboardMedium
import com.a.server.presentation.ui.WindowType
import com.a.server.presentation.ui.rememberWindowSize
import com.a.server.presentation.util.EffectLauncher
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

    when (windowSize) {
        WindowType.Compact -> {
            DashboardCompact(
                state = state,
                onAction = viewModel::onAction
            )
        }
        WindowType.Medium -> {
            DashboardMedium(
                state = state,
                onAction = viewModel::onAction
            )
        }
        WindowType.Expanded -> {
            DashboardExpanded(
                state = state,
                onAction = viewModel::onAction,
                snackBarHostState = snackBarHostState
            )
        }
    }
}