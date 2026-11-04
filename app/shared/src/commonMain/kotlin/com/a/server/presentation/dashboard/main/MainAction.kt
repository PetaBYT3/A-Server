package com.a.server.presentation.dashboard.main

import io.github.vinceglb.filekit.PlatformFile

sealed interface MainAction {
    data class Port(val newPort: String): MainAction
    data class Host(val newHost: String): MainAction
    data object SaveConfiguration: MainAction
    data object ToggleServer: MainAction

    data object ToggleTunnel: MainAction

    data class ChangeStorage(val platformFile: PlatformFile): MainAction
}