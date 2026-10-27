package com.a.server.presentation.dashboard

data class DashboardState(
    val isServerRunning: Boolean = false,
    val logs: List<String> = emptyList()
)
