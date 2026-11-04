package com.a.server.presentation.dashboard.layout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.a.server.presentation.dashboard.component.AuthenticationCard
import com.a.server.presentation.dashboard.component.DatabaseCard
import com.a.server.presentation.dashboard.component.ServerCard
import com.a.server.presentation.dashboard.component.StorageCard
import com.a.server.presentation.dashboard.component.TunnelCard
import com.a.server.presentation.dashboard.main.MainAction
import com.a.server.presentation.dashboard.main.MainState

@Composable
fun DashboardCompact(
    modifier: Modifier = Modifier,
    state: MainState,
    onAction: (MainAction) -> Unit
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize(),
        contentPadding = PaddingValues(start = 15.dp, end = 15.dp, bottom = 100.dp),
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        item("serverItem") {
            ServerCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .animateItem(),
                state = state,
                onPortChange = { onAction(MainAction.Port(it)) },
                onHostChange = { onAction(MainAction.Host(it)) },
                onSaveClick = { onAction(MainAction.SaveConfiguration) },
                onToggleClick = { onAction(MainAction.ToggleServer) }
            )
        }
        item("serverTunnelItem") {
            TunnelCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .animateItem(),
                state = state,
                onToggleClick = { onAction(MainAction.ToggleTunnel) }
            )
        }
        item("authenticationItem") {
            AuthenticationCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .animateItem(),
                state = state
            )
        }
        item("databaseItem") {
            DatabaseCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .animateItem(),
                state = state
            )
        }
        item("storageItem") {
            StorageCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .animateItem(),
                state = state,
                onChangeDirClick = { onAction(MainAction.ChangeStorage(it)) }
            )
        }
    }
}

@Composable
@Preview
private fun Preview() {
    DashboardCompact(
        state = MainState(),
        onAction = {}
    )
}