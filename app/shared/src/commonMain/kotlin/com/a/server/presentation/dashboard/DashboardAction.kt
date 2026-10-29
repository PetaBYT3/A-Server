package com.a.server.presentation.dashboard

sealed interface DashboardAction {
    data class Port(val newPort: String): DashboardAction
    data class Host(val newHost: String): DashboardAction
    data object SaveConfiguration: DashboardAction
    data object ToggleServer: DashboardAction

    data object ToggleTunnel: DashboardAction
}