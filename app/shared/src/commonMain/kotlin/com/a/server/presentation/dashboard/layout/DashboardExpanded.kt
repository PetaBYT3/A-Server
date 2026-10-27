package com.a.server.presentation.dashboard.layout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.a.server.presentation.dashboard.DashboardAction
import com.a.server.presentation.dashboard.DashboardState

@Composable
fun DashboardExpanded(
    modifier: Modifier = Modifier,
    state: DashboardState,
    onAction: (DashboardAction) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(start = 10.dp, end = 10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Button(
            onClick = { onAction(DashboardAction.ToggleServer) },
            content = {
                Text(
                    text = when (state.isServerRunning) {
                        true -> "Stop"
                        false -> "Start"
                    }
                )
            }
        )
        LazyColumn(
            modifier = Modifier
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(2.5.dp)
        ) {
            items(
                items = state.logs
            ) { log ->
                Text(text = log)
            }
        }
    }
}

@Composable
@Preview
private fun Preview() {
    DashboardExpanded(
        state = DashboardState(),
        onAction = {}
    )
}