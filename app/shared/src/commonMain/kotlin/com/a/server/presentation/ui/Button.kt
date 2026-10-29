@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package com.a.server.presentation.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularWavyProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ToggleButton
import androidx.compose.material3.ToggleButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer

@Composable
fun MediumToggleButtonWithLoading(
    modifier: Modifier = Modifier,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    text: String,
    isLoading: Boolean = false,
    enabled: Boolean = true
) {
    ToggleButton(
        modifier = modifier
            .height(ButtonDefaults.MediumContainerHeight),
        colors = ToggleButtonDefaults.colors(
            checkedContainerColor = MaterialTheme.colorScheme.error,
            checkedContentColor = MaterialTheme.colorScheme.onError,
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ),
        checked = checked,
        onCheckedChange = { if (!isLoading) onCheckedChange(it) },
        content = {
            Box(
                contentAlignment = Alignment.Center
            ) {
                Text(
                    modifier = Modifier
                        .graphicsLayer(alpha = if (isLoading) 0f else 1f),
                    text = text
                )
                CircularWavyProgressIndicator(
                    modifier = Modifier
                        .size(ButtonDefaults.MediumIconSize)
                        .graphicsLayer(alpha = if (isLoading) 1f else 0f),
                    color = MaterialTheme.colorScheme.onPrimary,
                    trackColor = MaterialTheme.colorScheme.onTertiary
                )
            }
        },
        enabled = enabled
    )
}