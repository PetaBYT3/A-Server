package com.a.server.presentation.tablesql

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.a.server.navigation.Navigator
import com.a.server.presentation.tablesql.layout.TableSqlCompact
import com.a.server.presentation.util.EffectLauncher
import org.koin.compose.koinInject

@Composable
fun TableSqlScreen(
    navigator: Navigator = koinInject(),
    viewModel: TableSqlViewModel = koinInject()
) {
    val state by viewModel.state.collectAsState()
    val snackBarHostState = remember { SnackbarHostState() }

    EffectLauncher(
        snackBarHostState = snackBarHostState,
        screenEffect = viewModel.effect
    )

    TableSqlCompact(
        navigator = navigator,
        state = state,
        onAction = viewModel::onAction,
        snackBarHostState = snackBarHostState
    )
}