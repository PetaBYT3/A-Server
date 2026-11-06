package com.a.server.presentation.dashboard.main

import androidx.compose.foundation.layout.Grid
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import aserver.app.shared.generated.resources.Res
import aserver.app.shared.generated.resources.host_24px
import aserver.app.shared.generated.resources.server_engine
import com.a.server.presentation.dashboard.main.component.MainCard
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
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
    ) {
        item {
            Grid(
                modifier = Modifier
                    .fillMaxWidth(),
                config = {}
            ) {
                MainCard(
                    icon = vectorResource(Res.drawable.host_24px),
                    isOnline = true,
                    title = stringResource(Res.string.server_engine),
                    uptime = "00:00:00, Day",
                    content = {

                    }
                )
            }
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