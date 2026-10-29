package com.a.server.presentation.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.dp

@Composable
fun rememberWindowSize(): WindowType {
    val density = LocalDensity.current
    val windowInfo = LocalWindowInfo.current
    val screenWidth = with(density) { windowInfo.containerSize.width.toDp() }

    return when {
        screenWidth < 600.dp -> WindowType.Compact
        screenWidth < 840.dp -> WindowType.Medium
        else -> WindowType.Expanded
    }
}

enum class WindowType {
    Compact, Medium, Expanded
}