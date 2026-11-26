package com.a.server.presentation.storage.filedetail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ContentCopy
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.QuestionMark
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import aserver.app.shared.generated.resources.Res
import aserver.app.shared.generated.resources.empty
import aserver.app.shared.generated.resources.error
import aserver.app.shared.generated.resources.file_local_direct_url
import aserver.app.shared.generated.resources.file_name
import aserver.app.shared.generated.resources.file_path
import aserver.app.shared.generated.resources.file_remote_direct_url
import aserver.app.shared.generated.resources.file_size
import com.a.server.core.manager.SystemManager
import com.a.server.presentation.ui.CenterContainedLoadingIndicator
import com.a.server.presentation.ui.MessageCard
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun FileDetailScreen(
    filePath: String,
    viewModel: FileDetailViewModel = koinViewModel(
        parameters = {
            parametersOf(filePath)
        }
    )
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val onAction = viewModel::onAction

    Content(
        state = state,
        onAction = onAction
    )
}

@Composable
private fun Content(
    state: FileDetailState,
    onAction: (FileDetailAction) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(start = 15.dp, end = 15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(
                modifier = Modifier
                    .weight(1f)
            )
            FilledTonalIconButton(
                onClick = {},
                content = { Icon(Icons.Rounded.Delete, null) }
            )
        }
        LazyColumn(
            modifier = Modifier
                .weight(1f),
            contentPadding = PaddingValues(start = 15.dp, end = 15.dp, bottom = 100.dp),
            verticalArrangement = Arrangement.spacedBy(ListItemDefaults.SegmentedGap)
        ) {
            if (state.isFileLoading) {
                item("isFileLoading") {
                    CenterContainedLoadingIndicator(
                        modifier = Modifier
                            .animateItem()
                    )
                }
                return@LazyColumn
            }
            if (state.isFileError != null) {
                item("isFileError") {
                    MessageCard(
                        modifier = Modifier
                            .animateItem(),
                        icon = Icons.Rounded.Warning,
                        title = stringResource(Res.string.error),
                        message = state.isFileError
                    )
                }
                return@LazyColumn
            }
            if (state.file == null) {
                item("isFileEmpty") {
                    MessageCard(
                        modifier = Modifier
                            .animateItem(),
                        icon = Icons.Rounded.QuestionMark,
                        title = stringResource(Res.string.empty),
                        message = "File Not Found"
                    )
                }
                return@LazyColumn
            }
            item("fileName") {
                SegmentedListItem(
                    modifier = Modifier
                        .animateItem(),
                    colors = ListItemDefaults.segmentedColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                    ),
                    shapes = ListItemDefaults.segmentedShapes(0, 5),
                    overlineContent = { Text(text = stringResource(Res.string.file_name)) },
                    content = {
                        Text(
                            text = state.file.name,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                )
            }
            item("fileSize") {
                SegmentedListItem(
                    modifier = Modifier
                        .animateItem(),
                    colors = ListItemDefaults.segmentedColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                    ),
                    shapes = ListItemDefaults.segmentedShapes(1, 5),
                    overlineContent = { Text(text = stringResource(Res.string.file_size)) },
                    content = {
                        Text(
                            text = state.file.size,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                )
            }
            item("filePath") {
                SegmentedListItem(
                    modifier = Modifier
                        .animateItem(),
                    colors = ListItemDefaults.segmentedColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                    ),
                    shapes = ListItemDefaults.segmentedShapes(2, 5),
                    overlineContent = { Text(text = stringResource(Res.string.file_path)) },
                    content = {
                        Text(
                            text = state.file.absolutePath,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                )
            }
            item("fileLocalDirectUrl") {
                SegmentedListItem(
                    modifier = Modifier
                        .animateItem(),
                    colors = ListItemDefaults.segmentedColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                    ),
                    shapes = ListItemDefaults.segmentedShapes(3, 5),
                    overlineContent = { Text(text = stringResource(Res.string.file_local_direct_url)) },
                    content = {
                        Text(
                            text = state.file.localDirectUrl,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    trailingContent = {
                        IconButton(
                            onClick = {
                                val systemManager = SystemManager()
                                systemManager.copyText(state.file.localDirectUrl)
                            },
                            content = { Icon(Icons.Rounded.ContentCopy, null) }
                        )
                    }
                )
            }
            item("fileRemoteDirectUrl") {
                SegmentedListItem(
                    modifier = Modifier
                        .animateItem(),
                    colors = ListItemDefaults.segmentedColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                    ),
                    shapes = ListItemDefaults.segmentedShapes(4, 5),
                    overlineContent = { Text(text = stringResource(Res.string.file_remote_direct_url)) },
                    content = {
                        Text(
                            text = state.file.remoteDirectUrl,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    trailingContent = {
                        IconButton(
                            onClick = {
                                val systemManager = SystemManager()
                                systemManager.copyText(state.file.remoteDirectUrl)
                            },
                            content = { Icon(Icons.Rounded.ContentCopy, null) }
                        )
                    }
                )
            }
        }
    }
}

@Composable
@Preview
private fun Preview() {
    Content(
        state = FileDetailState(),
        onAction = {}
    )
}