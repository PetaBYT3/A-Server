package com.a.server.presentation.dashboard.main.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Badge
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import aserver.app.shared.generated.resources.Res
import aserver.app.shared.generated.resources.base_url
import aserver.app.shared.generated.resources.cloud_24px
import aserver.app.shared.generated.resources.offline
import aserver.app.shared.generated.resources.online
import aserver.app.shared.generated.resources.start
import aserver.app.shared.generated.resources.stop
import aserver.app.shared.generated.resources.tunneling
import com.a.server.presentation.ui.ToggleButtonWithLoading
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

@Composable
fun TunnelCard(
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.surfaceContainer,
    contentColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    isOnline: Boolean,
    uptime: String,
    baseUrl: String,
    onToggleClick: () -> Unit,
    isLoading: Boolean = false
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
                    overlineContentColor = contentColor,
                    contentColor = contentColor,
                    trailingContentColor = contentColor
                ),
                overlineContent = {
                    Column {
                        Icon(
                            modifier = Modifier
                                .size(50.dp),
                            imageVector = vectorResource(Res.drawable.cloud_24px),
                            contentDescription = null
                        )
                        Text(text = stringResource(Res.string.tunneling))
                    }
                },
                content = {},
                trailingContent = {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        Text(text = uptime)
                        Badge(
                            containerColor = if (isOnline) {
                                MaterialTheme.colorScheme.primaryContainer
                            } else {
                                MaterialTheme.colorScheme.errorContainer
                            },
                            contentColor = if (isOnline) {
                                MaterialTheme.colorScheme.onPrimaryContainer
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
                    }
                }
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(5.dp),
                verticalAlignment = Alignment.Bottom
            ) {
                OutlinedTextField(
                    modifier = Modifier
                        .width(205.dp),
                    label = { Text(text = stringResource(Res.string.base_url)) },
                    value = baseUrl,
                    onValueChange = {},
                    singleLine = true,
                    readOnly = true
                )
                Spacer(
                    modifier = Modifier
                        .weight(1f)
                )
            }
            ToggleButtonWithLoading(
                checked = isOnline,
                onCheckedChange = { onToggleClick() },
                text = if (isOnline) {
                    stringResource(Res.string.stop)
                } else {
                    stringResource(Res.string.start)
                },
                isLoading = isLoading
            )
        }
    }
}

@Composable
@Preview
private fun Preview() {
    TunnelCard(
        isOnline = true,
        uptime = "00:00:00, 00 Days",
        baseUrl = "",
        onToggleClick = {}
    )
}