package com.a.server.presentation.dashboard.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Save
import androidx.compose.material3.Badge
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import aserver.app.shared.generated.resources.Res
import aserver.app.shared.generated.resources.cloud_24px
import aserver.app.shared.generated.resources.host_24px
import aserver.app.shared.generated.resources.offline
import aserver.app.shared.generated.resources.online
import aserver.app.shared.generated.resources.server_engine
import aserver.app.shared.generated.resources.start
import aserver.app.shared.generated.resources.stop
import aserver.app.shared.generated.resources.uptime
import com.a.server.presentation.dashboard.DashboardState
import com.a.server.presentation.ui.MediumToggleButtonWithLoading
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

@Composable
fun ServerCard(
    modifier: Modifier = Modifier,
    state: DashboardState,
    onPortChange: (String) -> Unit,
    onHostChange: (String) -> Unit,
    onSaveClick: () -> Unit,
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
                        imageVector = vectorResource(Res.drawable.host_24px),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
                content = {
                    Text(
                        text = stringResource(Res.string.server_engine),
                        fontWeight = FontWeight.Bold
                    )
                },
                supportingContent = {
                    Text(
                        text = buildString {
                            append("${stringResource(Res.string.uptime)}: ")
                            append(state.serverState.uptime)
                        }
                    )
                },
                trailingContent = {
                    Badge(
                        containerColor = when (state.serverState.isRunning) {
                            true -> MaterialTheme.colorScheme.primary
                            false -> MaterialTheme.colorScheme.error
                        }
                    ) {
                        Text(
                            text = stringResource(
                                resource = when (state.serverState.isRunning) {
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
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .widthIn(840.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedTextField(
                            modifier = Modifier
                                .fillMaxWidth(),
                            label = { Text(text = "Port") },
                            value = state.serverPort,
                            onValueChange = {
                                val filteredInput = it.filter { input ->
                                    input.isDigit()
                                }
                                onPortChange(filteredInput)
                            },
                            enabled = !state.serverState.isRunning,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Done
                            )
                        )
                        OutlinedTextField(
                            modifier = Modifier
                                .fillMaxWidth(),
                            label = { Text(text = "Host") },
                            value = state.serverHost,
                            onValueChange = {
                                val filteredInput = it.filter { input ->
                                    input.isDigit() || input == '.'
                                }
                                onHostChange(filteredInput)
                            },
                            enabled = !state.serverState.isRunning,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Done
                            )
                        )
                    }
                }
            )
            ListItem(
                colors = ListItemDefaults.colors(
                    containerColor = Color.Transparent
                ),
                content = {},
                trailingContent = {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        FilledTonalButton(
                            modifier = Modifier
                                .height(ButtonDefaults.MediumContainerHeight),
                            onClick = onSaveClick,
                            content = { Icon(Icons.Rounded.Save, null) },
                            enabled = !state.serverState.isRunning
                        )
                        MediumToggleButtonWithLoading(
                            checked = state.serverState.isRunning,
                            onCheckedChange = { onToggleClick() },
                            text = when (state.serverState.isRunning) {
                                true -> stringResource(Res.string.stop)
                                false -> stringResource(Res.string.start)
                            },
                            isLoading = state.isServerLoading
                        )
                    }
                }
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun Preview() {
    ServerCard(
        state = DashboardState(),
        onPortChange = {},
        onHostChange = {},
        onToggleClick = {},
        onSaveClick = {}
    )
}