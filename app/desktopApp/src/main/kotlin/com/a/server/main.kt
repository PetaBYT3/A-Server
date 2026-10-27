package com.a.server

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.a.server.data.di.DesktopModule
import com.a.server.di.initializeKoin
import com.a.server.presentation.App
import org.koin.plugin.module.dsl.module

fun main() = application {
    initializeKoin(
        config = {
            module<DesktopModule>()
        }
    )

    Window(
        onCloseRequest = ::exitApplication,
        title = "AServer",
    ) {
        App()
    }
}