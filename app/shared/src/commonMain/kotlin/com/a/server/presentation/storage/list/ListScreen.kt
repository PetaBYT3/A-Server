package com.a.server.presentation.storage.list

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CreateNewFolder
import androidx.compose.material.icons.rounded.Folder
import androidx.compose.material.icons.rounded.InsertDriveFile
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import aserver.app.shared.generated.resources.create_folder
import aserver.app.shared.generated.resources.empty
import aserver.app.shared.generated.resources.name
import com.a.server.domain.model.storage.FileModel
import com.a.server.navigation.NavigationKey
import com.a.server.navigation.NavigationState
import com.a.server.presentation.ui.CenterContainedLoadingIndicator
import com.a.server.presentation.ui.MessageCard
import com.a.server.presentation.ui.SimpleAlertDialog
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun StorageListScreen(
    navigationState: NavigationState = koinInject(),
    viewModel: ListViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Content(
        navigationState = navigationState,
        state = state,
        onAction = viewModel::onAction
    )

    SimpleAlertDialog(
        visible = state.isCreateFolderDialogVisible,
        onDismissRequest = { viewModel.onAction(ListAction.CreateFolderDialog) },
        icon = Icons.Rounded.CreateNewFolder,
        title = stringResource(Res.string.create_folder),
        content = {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth(),
                label = { Text(text = stringResource(Res.string.name)) },
                value = state.newFolderName,
                onValueChange = { viewModel.onAction(ListAction.FolderName(it)) }
            )
        },
        confirmButton = {
            viewModel.onAction(ListAction.CreateFolder)
            viewModel.onAction(ListAction.CreateFolderDialog)
        },
        dismissButton = {
            viewModel.onAction(ListAction.CreateFolderDialog)
        }
    )
}

@Composable
private fun Content(
    navigationState: NavigationState,
    state: ListState,
    onAction: (ListAction) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 15.dp, end = 15.dp)
                .height(56.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            FilledIconButton(
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                ),
                onClick = { onAction(ListAction.UpFolder) },
                content = { Icon(Icons.Rounded.KeyboardArrowUp, null) },
                enabled = state.storagePath != state.currentPath
            )
            FilledIconButton(
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                ),
                onClick = { onAction(ListAction.CreateFolderDialog) },
                content = { Icon(Icons.Rounded.CreateNewFolder, null) }
            )
            AnimatedContent(
                targetState = state.currentPath
            ) { targetState ->
                Text(
                    text = targetState?.removePrefix(state.storagePath ?: "") ?: "",
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
        LazyColumn(
            modifier = Modifier
                .weight(1f),
            contentPadding = PaddingValues(start = 15.dp, end = 15.dp, bottom = 100.dp),
            verticalArrangement = Arrangement.spacedBy(ListItemDefaults.SegmentedGap)
        ) {
            if (state.isFilesLoading) {
                item("isFilesLoading") {
                    CenterContainedLoadingIndicator(
                        modifier = Modifier
                            .animateItem()
                    )
                }
                return@LazyColumn
            }
            if (state.files.isEmpty()) {
                item("isFilesEmpty") {
                    MessageCard(
                        modifier = Modifier
                            .animateItem(),
                        icon = Icons.Rounded.Warning,
                        title = stringResource(Res.string.empty),
                        message = state.storagePath ?: "No Path"
                    )
                }
            }
            itemsIndexed(
                items = state.files,
                key = { _, fileModel -> fileModel.name }
            ) { index, fileModel ->
                SegmentedListItem(
                    modifier = Modifier
                        .animateItem(),
                    colors = ListItemDefaults.segmentedColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                    ),
                    shapes = ListItemDefaults.segmentedShapes(index, state.files.size),
                    onClick = {
                        if (fileModel.isFolder) {
                            onAction(ListAction.OpenFolder(fileModel.name))
                        } else {
                            val targetNavigationKey = NavigationKey.StorageDetail(
                                targetPath = fileModel.absolutePath
                            )
                            navigationState.navigateToDetail(targetNavigationKey)
                        }
                    },
                    leadingContent = {
                        val imageVector = if (fileModel.isFolder) {
                            Icons.Rounded.Folder
                        } else {
                            Icons.Rounded.InsertDriveFile
                        }
                        Icon(imageVector, null)
                    },
                    content = { Text(text = fileModel.name) },
                    trailingContent = {
                        if (!fileModel.isFolder) {
                            Text(text = fileModel.size)
                        }
                    }
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun Preview() {
    Content(
        navigationState = NavigationState(),
        state = ListState(
            isFilesLoading = false,
            currentPath = "preview/compose",
            files = listOf(
                FileModel(
                    name = "Script",
                    isFolder = true,
                    size = "1 KB",
                    absolutePath = ""
                ),
                FileModel(
                    name = "config.cfg",
                    isFolder = false,
                    size = "2 MB",
                    absolutePath = ""
                )
            )
        ),
        onAction = {}
    )
}