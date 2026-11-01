package com.a.server.presentation.auth

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import aserver.app.shared.generated.resources.Res
import aserver.app.shared.generated.resources.delete
import aserver.app.shared.generated.resources.users_delete_confirm
import com.a.server.navigation.Navigator
import com.a.server.presentation.auth.layout.AuthCompact
import com.a.server.presentation.ui.SimpleAlertDialog
import com.a.server.presentation.util.EffectLauncher
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AuthenticationScreen(
    navigator: Navigator = koinInject(),
    viewModel: AuthViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackBarHostState = remember { SnackbarHostState() }

    EffectLauncher(
        snackBarHostState = snackBarHostState,
        screenEffect = viewModel.effect
    )

    AuthCompact(
        navigator = navigator,
        state = state,
        onAction = viewModel::onAction,
        snackBarHostState = snackBarHostState
    )

    SimpleAlertDialog(
        visible = state.isDeleteDialogVisible,
        onDismissRequest = { viewModel.onAction(AuthAction.DeleteAlertDialog) },
        icon = Icons.Rounded.Delete,
        title = stringResource(Res.string.delete),
        text = stringResource(Res.string.users_delete_confirm),
        isCrucialAction = true,
        confirmButton = {
            viewModel.onAction(AuthAction.Delete)
            viewModel.onAction(AuthAction.DeleteAlertDialog)
        },
        dismissButton = { viewModel.onAction(AuthAction.DeleteAlertDialog) }
    )
}