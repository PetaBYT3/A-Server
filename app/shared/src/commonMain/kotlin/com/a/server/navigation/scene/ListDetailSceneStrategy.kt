package com.a.server.navigation.scene

import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.scene.Scene
import androidx.navigation3.scene.SceneStrategy
import androidx.navigation3.scene.SceneStrategyScope
import androidx.window.core.layout.WindowSizeClass

class ListDetailSceneStrategy <T: Any> (
    val windowSizeClass: WindowSizeClass,
): SceneStrategy<T> {
    override fun SceneStrategyScope<T>.calculateScene(
        entries: List<NavEntry<T>>
    ): Scene<T>? {
        if (!windowSizeClass.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND)) {
            return null
        }

        val listEntry = entries.findLast { navEntry ->
            navEntry.metadata.containsKey(ListDetailScene.LIST_KEY)
        } ?: return null

        val detailEntry = entries.lastOrNull()?.takeIf { navEntry ->
            navEntry.metadata.containsKey(ListDetailScene.DETAIL_KEY)
        }

        return ListDetailScene(
            listEntries = listEntry,
            detailEntry = detailEntry,
            key = listEntry.contentKey,
            previousEntries = entries.dropLast(1)
        )
    }
}

@Composable
fun <T: Any> rememberListDetailSceneStrategy(): ListDetailSceneStrategy<T> {
    val windowAdaptiveInfo = currentWindowAdaptiveInfo()
    val windowSizeClass = windowAdaptiveInfo.windowSizeClass

    return remember(windowSizeClass) {
        ListDetailSceneStrategy(windowSizeClass = windowSizeClass)
    }
}