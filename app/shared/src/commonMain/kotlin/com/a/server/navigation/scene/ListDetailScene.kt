@file:Suppress("UNCHECKED_CAST")

package com.a.server.navigation.scene

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.scene.Scene
import com.a.server.presentation.ui.consumeStartSpace

class ListDetailScene <T: Any> (
    val listEntries: NavEntry<T>,
    val detailEntry: NavEntry<T>?,
    override val key: Any,
    override val previousEntries: List<NavEntry<T>>
): Scene<T> {
    override val entries: List<NavEntry<T>> = listOfNotNull(listEntries, detailEntry)

    override val content: @Composable () -> Unit = {
        val placeholder = listEntries.metadata[PLACEHOLDER_KEY] as? @Composable () -> Unit

        Row(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.4f)
                    .fillMaxHeight()
            ) {
                listEntries.Content()
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .consumeStartSpace(15.dp)
            ) {
                AnimatedContent(detailEntry) { targetScene ->
                    if (targetScene != null) {
                        targetScene.Content()
                    } else {
                        placeholder?.invoke()
                    }
                }
            }
        }
    }

    companion object {
        const val LIST_KEY = "ListDetailScene-List"
        const val DETAIL_KEY = "ListDetailScene-Detail"
        const val PLACEHOLDER_KEY = "ListDetailScene-Placeholder"

        fun listPane(placeholder: @Composable () -> Unit): Map<String, Any> {
            return mapOf(
                LIST_KEY to true,
                PLACEHOLDER_KEY to placeholder
            )
        }
        fun detailPane(): Map<String, Any> {
            return mapOf(DETAIL_KEY to true)
        }
    }
}