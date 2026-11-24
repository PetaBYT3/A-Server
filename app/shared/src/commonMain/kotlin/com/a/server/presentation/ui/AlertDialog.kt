package com.a.server.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun SimpleAlertDialog(
    visible: Boolean,
    icon: ImageVector,
    title: String,
    content: @Composable () -> Unit,
    isCrucialAction: Boolean = false,
    confirmButton: () -> Unit,
    dismissButton: () -> Unit
) {
    if (visible) {
        AlertDialog(
            onDismissRequest = dismissButton,
            icon = { Icon(icon, null) },
            title = { Text(text = title) },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    content()
                }
            },
            confirmButton = {
                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isCrucialAction) {
                            MaterialTheme.colorScheme.error
                        } else {
                            MaterialTheme.colorScheme.primary
                        },
                        contentColor = if (isCrucialAction) {
                            MaterialTheme.colorScheme.onError
                        } else {
                            MaterialTheme.colorScheme.onPrimary
                        }
                    ),
                    onClick = confirmButton,
                    content = { Text(text = "Confirm") }
                )
            },
            dismissButton = {
                FilledTonalButton(
                    onClick = dismissButton,
                    content = { Text(text = "Cancel") }
                )
            }
        )
    }
}