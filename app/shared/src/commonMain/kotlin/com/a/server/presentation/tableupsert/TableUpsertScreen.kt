package com.a.server.presentation.tableupsert

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.a.server.navigation.Navigator
import com.a.server.presentation.tableupsert.layout.TableUpsertCompact
import com.a.server.presentation.util.EffectLauncher
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun TableUpsertScreen(
    tableId: String,
    navigator: Navigator = koinInject(),
    viewModel: TableUpsertViewModel = koinViewModel(
        parameters = {
            parametersOf(tableId)
        }
    )
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackBarHostState = remember { SnackbarHostState() }

    EffectLauncher(
        snackBarHostState = snackBarHostState,
        screenEffect = viewModel.effect
    )

    TableUpsertCompact(
        navigator = navigator,
        state = state,
        onAction = viewModel::onAction,
        snackBarHostState = snackBarHostState
    )
}