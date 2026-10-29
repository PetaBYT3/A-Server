package com.a.server.presentation.dashboard.layout

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.a.server.presentation.dashboard.DashboardAction
import com.a.server.presentation.dashboard.DashboardState

@Composable
fun DashboardCompact(
    state: DashboardState,
    onAction: (DashboardAction) -> Unit
) {
    Text(text = "Compact")
}

@Composable
@Preview
private fun Preview() {
    DashboardCompact(
        state = DashboardState(),
        onAction = {}
    )
}