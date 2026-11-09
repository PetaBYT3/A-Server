package com.a.server.presentation.dashboard.main.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import aserver.app.shared.generated.resources.Res
import aserver.app.shared.generated.resources.host
import aserver.app.shared.generated.resources.host_24px
import aserver.app.shared.generated.resources.offline
import aserver.app.shared.generated.resources.online
import aserver.app.shared.generated.resources.server_engine
import aserver.app.shared.generated.resources.start
import aserver.app.shared.generated.resources.stop
import com.a.server.presentation.ui.ToggleButtonWithLoading
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

@Composable
fun ServerCard(
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.surfaceContainer,
    contentColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    isOnline: Boolean,
    uptime: String,
    host: String,
    onHostChange: (String) -> Unit,
    port: String,
    onPortChange: (String) -> Unit,
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
                            imageVector = vectorResource(Res.drawable.host_24px),
                            contentDescription = null
                        )
                        Text(text = stringResource(Res.string.server_engine))
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
                        .width(125.dp),
                    label = { Text(text = stringResource(Res.string.host)) },
                    value = host,
                    onValueChange = {
                        val filteredInput = it.filter { input ->
                            input.isDigit() || input == '.'
                        }
                        onHostChange(filteredInput)
                    },
                    singleLine = true,
                    enabled = !isOnline,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    )
                )
                OutlinedTextField(
                    modifier = Modifier
                        .width(75.dp),
                    label = { Text(text = "Port") },
                    value = port,
                    onValueChange = {
                        val filteredInput = it.filter { input ->
                            input.isDigit()
                        }
                        onPortChange(filteredInput)
                    },
                    singleLine = true,
                    enabled = !isOnline,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    )
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
    ServerCard(
        isOnline = false,
        uptime = "00:00:00, 00 Days",
        host = "0.0.0.0",
        onHostChange = {},
        port = "8080",
        onPortChange = {},
        onToggleClick = {}
    )
}