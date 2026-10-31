package com.a.server.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun MessageCard(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    title: String,
    message: String
) {
    Card(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier,
            verticalArrangement = Arrangement.spacedBy(5.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ListItem(
                colors = ListItemDefaults.colors(
                    containerColor = Color.Transparent
                ),
                overlineContent = {
                    Icon(
                        modifier = Modifier
                            .size(50.dp),
                        imageVector = icon,
                        contentDescription = null
                    )
                },
                content = {
                    Text(text = title)
                },
                supportingContent = {
                    Text(text = message)
                }
            )
        }
    }
}

@Composable
@Preview
private fun Preview() {
    MessageCard(
        icon = Icons.Rounded.Warning,
        title = "Warning",
        message = "Message Preview"
    )
}