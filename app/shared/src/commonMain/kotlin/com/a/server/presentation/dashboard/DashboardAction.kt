package com.a.server.presentation.dashboard

sealed interface DashboardAction {
    data object ToggleServer: DashboardAction
}