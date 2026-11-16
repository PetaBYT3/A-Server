@file:Suppress("UNCHECKED_CAST")

package com.a.server.navigation.scene

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.scene.Scene
import com.a.server.presentation.ui.DirectionalAnimatedVisibility
import com.a.server.presentation.ui.VisibilityDirection
import com.a.server.presentation.ui.consumeStartSpace

class ListDetailScene <T: Any> (
    val listEntries: NavEntry<T>,
    val detailEntry: NavEntry<T>?,
    val extraEntry: NavEntry<T>?,
    override val key: Any,
    override val previousEntries: List<NavEntry<T>>
): Scene<T> {
    override val entries: List<NavEntry<T>> = listOfNotNull(
        listEntries,
        detailEntry,
        extraEntry
    )

    override val content: @Composable () -> Unit = {
        val placeholder = listEntries.metadata[PLACEHOLDER_KEY] as? @Composable () -> Unit
        val hasExtra = extraEntry != null

        Row(
            modifier = Modifier
                .fillMaxSize()
        ) {
            val listWeight by animateFloatAsState(
                targetValue = if (hasExtra) 0f else 0.4f
            )
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(listWeight)
            ) {
                listEntries.Content()
            }

            val detailWeight = if (hasExtra) 1f else 0.6f
            Row(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(detailWeight)
                    .consumeStartSpace(15.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f)
                ) {
                    AnimatedContent(detailEntry) { targetScene ->
                        if (targetScene != null) {
                            targetScene.Content()
                        } else {
                            placeholder?.invoke()
                        }
                    }
                }

                DirectionalAnimatedVisibility(
                    visible = hasExtra,
                    direction = VisibilityDirection.Start,
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f)
                        .consumeStartSpace(15.dp)
                ) {
                    extraEntry?.let { extra ->
                        Column(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            extra.Content()
                        }
                    }
                }
            }
        }
    }

    companion object {
        const val LIST_KEY = "ListDetailScene-List"
        const val DETAIL_KEY = "ListDetailScene-Detail"
        const val EXTRA_KEY = "ListDetailExtraScene-Extra"
        const val PLACEHOLDER_KEY = "ListDetailScene-Placeholder"

        fun listPane(placeholder: @Composable (() -> Unit)): Map<String, Any> {
            return mapOf(
                LIST_KEY to true,
                PLACEHOLDER_KEY to placeholder
            )
        }

        fun detailPane(): Map<String, Any> {
            return mapOf(DETAIL_KEY to true)
        }

        fun extraPane(): Map<String, Any> {
            return mapOf(EXTRA_KEY to true)
        }
    }
}