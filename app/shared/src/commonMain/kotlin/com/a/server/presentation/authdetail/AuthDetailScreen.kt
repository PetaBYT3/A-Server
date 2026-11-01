package com.a.server.presentation.authdetail

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.a.server.navigation.Navigator
import com.a.server.presentation.authdetail.layout.AuthDetailCompact
import com.a.server.presentation.util.EffectLauncher
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun AuthDetailScreen(
    authId: String,
    navigator: Navigator = koinInject(),
    viewModel: AuthDetailViewModel = koinViewModel(
        parameters = {
            parametersOf(authId)
        }
    )
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackBarHostState = remember { SnackbarHostState() }

    EffectLauncher(
        snackBarHostState = snackBarHostState,
        screenEffect = viewModel.effect
    )

    AuthDetailCompact(
        navigator = navigator,
        state = state,
        onAction = viewModel::onAction,
        snackBarHostState = snackBarHostState
    )
}