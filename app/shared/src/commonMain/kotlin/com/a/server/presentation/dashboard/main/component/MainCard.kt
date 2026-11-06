package com.a.server.presentation.dashboard.main.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Badge
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import aserver.app.shared.generated.resources.Res
import aserver.app.shared.generated.resources.offline
import aserver.app.shared.generated.resources.online
import org.jetbrains.compose.resources.stringResource

@Composable
fun MainCard(
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.surfaceContainer,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    icon: ImageVector,
    isOnline: Boolean,
    title: String,
    uptime: String,
    content: @Composable (() -> Unit)
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = containerColor,
            contentColor = contentColor
        )
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            ListItem(
                contentPadding = PaddingValues(0.dp),
                colors = ListItemDefaults.colors(
                    containerColor = Color.Transparent,
                    contentColor = contentColor
                ),
                overlineContent = {
                    Badge(
                        containerColor = if (isOnline) {
                            MaterialTheme.colorScheme.secondaryContainer
                        } else {
                            MaterialTheme.colorScheme.errorContainer
                        },
                        contentColor = if (isOnline) {
                            MaterialTheme.colorScheme.onSecondaryContainer
                        } else {
                            MaterialTheme.colorScheme.onErrorContainer
                        }
                    ) {
                        Text(
                            text = if (isOnline) {
                                stringResource(Res.string.online)
                            } else {
                                stringResource(Res.string.offline)
                            }
                        )
                    }
                },
                leadingContent = {
                    Icon(
                        modifier = Modifier
                            .size(40.dp),
                        imageVector = icon,
                        contentDescription = null
                    )
                },
                content = { Text(text = title) },
                trailingContent = { Text(text = uptime) }
            )
            content()
        }
    }
}

@Composable
@Preview
private fun Preview() {

}