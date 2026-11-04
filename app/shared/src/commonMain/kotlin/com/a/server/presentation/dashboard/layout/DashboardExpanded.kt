package com.a.server.presentation.dashboard.layout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
fun DashboardExpanded(
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
        item("serverTunnelItem") {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)
                    .animateItem(),
                horizontalArrangement = Arrangement.spacedBy(15.dp)
            ) {
                ServerCard(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f),
                    state = state,
                    onPortChange = { onAction(MainAction.Port(it)) },
                    onHostChange = { onAction(MainAction.Host(it)) },
                    onSaveClick = { onAction(MainAction.SaveConfiguration) },
                    onToggleClick = { onAction(MainAction.ToggleServer) }
                )
                TunnelCard(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f),
                    state = state,
                    onToggleClick = { onAction(MainAction.ToggleTunnel) }
                )
            }
        }
        item("authenticationDatabaseStorageItem") {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)
                    .animateItem(),
                horizontalArrangement = Arrangement.spacedBy(15.dp)
            ) {
                AuthenticationCard(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(0.33f),
                    state = state
                )
                DatabaseCard(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(0.33f),
                    state = state
                )
                StorageCard(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(0.33f),
                    state = state,
                    onChangeDirClick = { onAction(MainAction.ChangeStorage(it)) }
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true, widthDp = 1000)
private fun Preview() {
    DashboardExpanded(
        state = MainState(),
        onAction = {}
    )
}