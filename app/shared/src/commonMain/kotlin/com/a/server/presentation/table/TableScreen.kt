package com.a.server.presentation.table

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.a.server.navigation.Navigator
import com.a.server.presentation.table.layout.TableCompact
import com.a.server.presentation.util.EffectLauncher
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun TableScreen(
    navigator: Navigator = koinInject(),
    viewModel: TableViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackBarHostState = remember { SnackbarHostState() }

    EffectLauncher(
        snackBarHostState = snackBarHostState,
        screenEffect = viewModel.effect
    )

    TableCompact(
        navigator = navigator,
        state = state,
        onAction = viewModel::onAction,
        snackBarHostState = snackBarHostState
    )
}