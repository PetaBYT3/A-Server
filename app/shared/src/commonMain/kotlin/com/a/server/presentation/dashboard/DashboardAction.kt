package com.a.server.presentation.dashboard

import io.github.vinceglb.filekit.PlatformFile

sealed interface DashboardAction {
    data class Port(val newPort: String): DashboardAction
    data class Host(val newHost: String): DashboardAction
    data object SaveConfiguration: DashboardAction
    data object ToggleServer: DashboardAction

    data object ToggleTunnel: DashboardAction

    data class ChangeStorage(val platformFile: PlatformFile): DashboardAction
}