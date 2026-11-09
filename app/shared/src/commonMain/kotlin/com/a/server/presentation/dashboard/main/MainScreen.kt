package com.a.server.presentation.dashboard.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import aserver.app.shared.generated.resources.Res
import aserver.app.shared.generated.resources.authentication
import aserver.app.shared.generated.resources.database
import aserver.app.shared.generated.resources.database_24px
import aserver.app.shared.generated.resources.storage
import aserver.app.shared.generated.resources.storage_24px
import com.a.server.presentation.dashboard.main.component.MainCard
import com.a.server.presentation.dashboard.main.component.ServerCard
import com.a.server.presentation.dashboard.main.component.TunnelCard
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun DashboardMainScreen(
    viewModel: MainViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Content(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
private fun Content(
    state: MainState,
    onAction: (MainAction) -> Unit
) {
    LazyVerticalGrid(
        modifier = Modifier
            .fillMaxSize(),
        columns = GridCells.Adaptive(300.dp),
        contentPadding = PaddingValues(start = 15.dp, end = 15.dp, bottom = 100.dp),
        verticalArrangement = Arrangement.spacedBy(15.dp),
        horizontalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        item {
            ServerCard(
                isOnline = state.serverState.isRunning,
                uptime = state.serverState.uptime,
                host = state.serverHost,
                onHostChange = { onAction(MainAction.Host(it)) },
                port = state.serverPort,
                onPortChange = { onAction(MainAction.Port(it)) },
                onToggleClick = { onAction(MainAction.ToggleServer) },
                isLoading = state.isServerLoading
            )
        }
        item {
            TunnelCard(
                isOnline = state.tunnelState.isRunning,
                uptime = state.tunnelState.uptime,
                baseUrl = state.tunnelState.publicUrl ?: "",
                onToggleClick = { onAction(MainAction.ToggleTunnel) },
                isLoading = state.isTunnelLoading
            )
        }
        item() {
            MainCard(
                icon = Icons.Rounded.Lock,
                title = stringResource(Res.string.authentication),
                content = {},
                trailingContent = {}
            )
        }
        item() {
            MainCard(
                icon = vectorResource(Res.drawable.database_24px),
                title = stringResource(Res.string.database),
                content = {},
                trailingContent = {}
            )
        }
        item() {
            MainCard(
                icon = vectorResource(Res.drawable.storage_24px),
                title = stringResource(Res.string.storage),
                content = {},
                trailingContent = {}
            )
        }
    }
}


@Composable
@Preview
private fun Preview() {
    Content(
        state = MainState(),
        onAction = {}
    )
}