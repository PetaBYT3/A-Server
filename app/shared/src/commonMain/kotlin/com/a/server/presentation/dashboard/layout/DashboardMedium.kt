package com.a.server.presentation.dashboard.layout

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.a.server.presentation.dashboard.DashboardAction
import com.a.server.presentation.dashboard.DashboardState

@Composable
fun DashboardMedium(
    state: DashboardState,
    onAction: (DashboardAction) -> Unit
) {
    Text(text = "Medium")
}