package com.a.server.presentation.dashboard.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ContentCopy
import androidx.compose.material.icons.rounded.FolderOpen
import androidx.compose.material3.Card
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import aserver.app.shared.generated.resources.Res
import aserver.app.shared.generated.resources.storage
import aserver.app.shared.generated.resources.storage_24px
import com.a.server.core.manager.SystemManager
import com.a.server.presentation.dashboard.main.MainState
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.dialogs.compose.rememberDirectoryPickerLauncher
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

@Composable
fun StorageCard(
    modifier: Modifier = Modifier,
    state: MainState,
    onChangeDirClick: (PlatformFile) -> Unit
) {
    val directoryLauncher = rememberDirectoryPickerLauncher(
        onResult = { platformFile ->
            if (platformFile != null) {
                onChangeDirClick(platformFile)
            }
        }
    )

    Card(
        modifier = modifier,
        shape = AbsoluteRoundedCornerShape(16.dp)
    ) {
        Column {
            ListItem(
                colors = ListItemDefaults.colors(
                    containerColor = Color.Transparent
                ),
                leadingContent = {
                    Icon(
                        imageVector = vectorResource(Res.drawable.storage_24px),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
                content = {
                    Text(
                        text = stringResource(Res.string.storage),
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                supportingContent = {
                    Text(
                        text = state.storageState.usedAndFreeSize,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                trailingContent = {
                    FilledTonalIconButton(
                        onClick = { directoryLauncher.launch() },
                        content = { Icon(Icons.Rounded.FolderOpen, null) }
                    )
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
                        label = { Text(text = "Path") },
                        value = state.storagePath,
                        onValueChange = {},
                        readOnly = true,
                        singleLine = true,
                        trailingIcon = {
                            IconButton(
                                onClick = { SystemManager().copyText(state.storagePath) },
                                content = { Icon(Icons.Rounded.ContentCopy, null) }
                            )
                        }
                    )
                }
            )
        }
    }
}

@Composable
@Preview
private fun Preview() {
    StorageCard(
        state = MainState(),
        onChangeDirClick = {  }
    )
}