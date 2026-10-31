package com.a.server.presentation.authentication

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import aserver.app.shared.generated.resources.Res
import aserver.app.shared.generated.resources.authentication
import aserver.app.shared.generated.resources.delete
import aserver.app.shared.generated.resources.users_delete_confirm
import com.a.server.navigation.Navigator
import com.a.server.presentation.authentication.layout.AuthenticationCompact
import com.a.server.presentation.authentication.layout.AuthenticationExpanded
import com.a.server.presentation.ui.SimpleAlertDialog
import com.a.server.presentation.ui.SlideUpAnimatedVisibility
import com.a.server.presentation.ui.TransparentLargeTopAppBar
import com.a.server.presentation.ui.WindowType
import com.a.server.presentation.ui.rememberWindowSize
import com.a.server.presentation.util.EffectLauncher
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AuthenticationScreen(
    navigator: Navigator,
    viewModel: AuthenticationViewModel = koinViewModel()
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
                onNavigationClick = { navigator.navigateBack() },
                title = stringResource(Res.string.authentication)
            )
        },
        content = { innerPadding ->
            when (windowSize) {
                WindowType.Compact -> {
                    AuthenticationCompact(
                        modifier = Modifier
                            .padding(innerPadding),
                        navigator = navigator,
                        state = state,
                        onAction = viewModel::onAction
                    )
                }
                else -> {
                    AuthenticationExpanded(
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
        },
        floatingActionButton = {
            SlideUpAnimatedVisibility(
                visible = state.selectedAuthenticationId.isNotEmpty()
            ) {
                ExtendedFloatingActionButton(
                    onClick = { viewModel.onAction(AuthenticationAction.DeleteAlertDialog) },
                    icon = { Icon(Icons.Rounded.Delete, null) },
                    text = { Text(text = stringResource(Res.string.delete)) },
                )
            }
        }
    )

    SimpleAlertDialog(
        visible = state.isDeleteDialogVisible,
        onDismissRequest = { viewModel.onAction(AuthenticationAction.DeleteAlertDialog) },
        icon = Icons.Rounded.Delete,
        title = stringResource(Res.string.delete),
        text = stringResource(Res.string.users_delete_confirm),
        isCrucialAction = true,
        confirmButton = {
            viewModel.onAction(AuthenticationAction.Delete)
            viewModel.onAction(AuthenticationAction.DeleteAlertDialog)
        },
        dismissButton = { viewModel.onAction(AuthenticationAction.DeleteAlertDialog) }
    )
}