package com.a.server.presentation.storage.filelist

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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Deselect
import androidx.compose.material.icons.rounded.InsertDriveFile
import androidx.compose.material.icons.rounded.QuestionMark
import androidx.compose.material.icons.rounded.SelectAll
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.FilledTonalIconToggleButton
import androidx.compose.material3.Icon
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
import com.a.server.navigation.NavigationKey
import com.a.server.navigation.NavigationState
import com.a.server.presentation.ui.CenterContainedLoadingIndicator
import com.a.server.presentation.ui.MessageCard
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun FileListScreen(
    folderName: String,
    navigationState: NavigationState = koinInject(),
    viewModel: FileListViewModel = koinViewModel(
        parameters = {
            parametersOf(folderName)
        }
    )
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Content(
        folderName = folderName,
        navigationState = navigationState,
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
private fun Content(
    folderName: String,
    navigationState: NavigationState,
    state: FileListState,
    onAction: (FileListlAction) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        Row(
            modifier = Modifier
                .height(56.dp)
                .padding(start = 15.dp, end = 15.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            FilledTonalIconToggleButton(
                checked = state.fileList.size == state.selectedFile.size,
                onCheckedChange = { checked ->
                    if (checked) {
                        onAction(FileListlAction.SelectAllFiles)
                    } else {
                        onAction(FileListlAction.UnselectAllFiles)
                    }
                },
                content = {
                    Icon(
                        imageVector = if (state.fileList.size == state.selectedFile.size)
                            Icons.Rounded.Deselect else Icons.Rounded.SelectAll,
                        contentDescription = null
                    )
                }
            )
            Spacer(
                modifier = Modifier
                    .weight(1f)
            )
            FilledTonalIconButton(
                enabled = state.selectedFile.isNotEmpty(),
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
            if (state.isFileListLoading) {
                item("isFileListLoading") {
                    CenterContainedLoadingIndicator(
                        modifier = Modifier
                            .animateItem()
                    )
                }
                return@LazyColumn
            }
            if (state.isFileListError != null) {
                item("isFileListError") {
                    MessageCard(
                        modifier = Modifier,
                        icon = Icons.Rounded.Warning,
                        title = stringResource(Res.string.error),
                        message = state.isFileListError
                    )
                }
                return@LazyColumn
            }
            if (state.fileList.isEmpty()) {
                item("isFileListEmpty") {
                    MessageCard(
                        modifier = Modifier,
                        icon = Icons.Rounded.QuestionMark,
                        title = stringResource(Res.string.empty),
                        message = ""
                    )
                }
                return@LazyColumn
            }
            itemsIndexed(
                items = state.fileList,
                key = { _, fileModel -> fileModel.absolutePath }
            ) { index, fileModel ->
                SegmentedListItem(
                    modifier = Modifier
                        .animateItem(),
                    onClick = {
                        val targetNavigation = NavigationKey.FileDetailScreen(
                            filePath = "$folderName/${fileModel.name}"
                        )
                        navigationState.navigateTo(targetNavigation)
                    },
                    selected = fileModel in state.selectedFile,
                    colors = ListItemDefaults.segmentedColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                    ),
                    shapes = ListItemDefaults.segmentedShapes(index, state.fileList.size),
                    leadingContent = {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(15.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = fileModel in state.selectedFile,
                                onCheckedChange = { checked ->
                                    if (!checked) {
                                        onAction(FileListlAction.UnselectFile(fileModel))
                                    } else {
                                        onAction(FileListlAction.SelectFile(fileModel))
                                    }
                                }
                            )
                            Icon(Icons.Rounded.InsertDriveFile, null)
                        }
                    },
                    content = {
                        Text(
                            text = fileModel.name,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    trailingContent = { Text(text = fileModel.size) }
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun Preview() {
    Content(
        folderName = "",
        navigationState = NavigationState(),
        state = FileListState(
            isFileListLoading = false
        ),
        onAction = {}
    )
}