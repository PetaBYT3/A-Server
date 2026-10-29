package com.a.server.presentation.dashboard.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ContentCopy
import androidx.compose.material3.Badge
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.ToggleButton
import androidx.compose.material3.ToggleButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import aserver.app.shared.generated.resources.Res
import aserver.app.shared.generated.resources.base_url
import aserver.app.shared.generated.resources.cloud_24px
import aserver.app.shared.generated.resources.offline
import aserver.app.shared.generated.resources.online
import aserver.app.shared.generated.resources.start
import aserver.app.shared.generated.resources.stop
import aserver.app.shared.generated.resources.tunneling
import aserver.app.shared.generated.resources.uptime
import com.a.server.core.manager.SystemManager
import com.a.server.presentation.dashboard.DashboardState
import com.a.server.presentation.ui.MediumToggleButtonWithLoading
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

@Composable
fun TunnelCard(
    modifier: Modifier = Modifier,
    state: DashboardState,
    onToggleClick: () -> Unit
) {
    Card(
        modifier = modifier
    ) {
        Column {
            ListItem(
                colors = ListItemDefaults.colors(
                    containerColor = Color.Transparent
                ),
                leadingContent = {
                    Icon(
                        imageVector = vectorResource(Res.drawable.cloud_24px),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
                content = {
                    Text(
                        text = stringResource(Res.string.tunneling),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.Bold
                    )
                },
                supportingContent = {
                    Text(
                        text = buildString {
                            append("${stringResource(Res.string.uptime)}: ")
                            append(state.tunnelState.uptime)
                        },
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                trailingContent = {
                    Badge(
                        containerColor = when (state.tunnelState.isRunning) {
                            true -> MaterialTheme.colorScheme.primary
                            false -> MaterialTheme.colorScheme.error
                        }
                    ) {
                        Text(
                            text = stringResource(
                                resource = when (state.tunnelState.isRunning) {
                                    true -> Res.string.online
                                    false -> Res.string.offline
                                }
                            )
                        )
                    }
                }
            )
            ListItem(
                colors = ListItemDefaults.colors(
                    containerColor = Color.Transparent
                ),
                content = {
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth(),
                        label = { Text(text = stringResource(Res.string.base_url)) },
                        value = state.tunnelState.publicUrl ?: "",
                        onValueChange = {},
                        readOnly = true,
                        singleLine = true,
                        trailingIcon = {
                            IconButton(
                                onClick = {
                                    state.tunnelState.publicUrl?.let { publicUrl ->
                                        SystemManager().copyText(publicUrl)
                                    }
                                },
                                content = { Icon(Icons.Rounded.ContentCopy, null) }
                            )
                        }
                    )
                }
            )
            Spacer(
                modifier = Modifier
                    .weight(1f)
            )
            ListItem(
                colors = ListItemDefaults.colors(
                    containerColor = Color.Transparent
                ),
                content = {},
                trailingContent = {
                    MediumToggleButtonWithLoading(
                        checked = state.tunnelState.isRunning,
                        onCheckedChange = { onToggleClick() },
                        text = when (state.tunnelState.isRunning) {
                            true -> stringResource(Res.string.stop)
                            false -> stringResource(Res.string.start)
                        },
                        isLoading = state.isTunnelLoading
                    )
                }
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun Preview() {
    TunnelCard(
        state = DashboardState(),
        onToggleClick = {}
    )
}