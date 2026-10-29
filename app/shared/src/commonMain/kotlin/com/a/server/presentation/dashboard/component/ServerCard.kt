package com.a.server.presentation.dashboard.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Badge
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import aserver.app.shared.generated.resources.Res
import aserver.app.shared.generated.resources.cloud_24px
import aserver.app.shared.generated.resources.offline
import aserver.app.shared.generated.resources.online
import aserver.app.shared.generated.resources.server_engine
import aserver.app.shared.generated.resources.start
import aserver.app.shared.generated.resources.stop
import aserver.app.shared.generated.resources.uptime
import com.a.server.presentation.dashboard.DashboardState
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

@Composable
fun ServerCard(
    modifier: Modifier = Modifier,
    state: DashboardState,
    onPortChange: (String) -> Unit,
    onHostChange: (String) -> Unit,
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
                        text = stringResource(Res.string.server_engine),
                        fontWeight = FontWeight.Bold
                    )
                },
                supportingContent = {
                    Text(
                        text = buildString {
                            append("${stringResource(Res.string.uptime)}: ")
                            append(state.serverUptime)
                        }
                    )
                },
                trailingContent = {
                    Badge(
                        containerColor = when (state.isServerRunning) {
                            true -> MaterialTheme.colorScheme.primary
                            false -> MaterialTheme.colorScheme.error
                        }
                    ) {
                        Text(
                            text = stringResource(
                                resource = when (state.isServerRunning) {
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
                                    input.isDigit() || input == '.'
                                }
                                onPortChange(filteredInput)
                            },
                            enabled = !state.isServerRunning,
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
                            enabled = !state.isServerRunning,
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
                    ToggleButton(
                        modifier = Modifier
                            .height(ButtonDefaults.MediumContainerHeight),
                        colors = ToggleButtonDefaults.colors(
                            checkedContainerColor = MaterialTheme.colorScheme.error,
                            checkedContentColor = MaterialTheme.colorScheme.onError,
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        ),
                        checked = state.isServerRunning,
                        onCheckedChange = { onToggleClick() },
                        content = {
                            Text(
                                text = stringResource(
                                    resource = when (state.isServerRunning) {
                                        true -> Res.string.stop
                                        false -> Res.string.start
                                    }
                                )
                            )
                        }
                    )
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
        onToggleClick = {}
    )
}