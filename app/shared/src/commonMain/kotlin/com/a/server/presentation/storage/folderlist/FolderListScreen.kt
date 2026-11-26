package com.a.server.presentation.storage.folderlist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.CreateNewFolder
import androidx.compose.material.icons.rounded.Folder
import androidx.compose.material.icons.rounded.QuestionMark
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedListItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import aserver.app.shared.generated.resources.error
import aserver.app.shared.generated.resources.name
import aserver.app.shared.generated.resources.search
import com.a.server.navigation.NavigationKey
import com.a.server.navigation.NavigationState
import com.a.server.presentation.ui.CenterContainedLoadingIndicator
import com.a.server.presentation.ui.MessageCard
import com.a.server.presentation.ui.SimpleAlertDialog
import com.a.server.presentation.ui.SimpleTextFieldSearchBar
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun FolderListScreen(
    navigationState: NavigationState = koinInject(),
    viewModel: FolderListViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val onAction = viewModel::onAction

    Content(
        navigationState = navigationState,
        state = state,
        onAction = onAction
    )

    SimpleAlertDialog(
        visible = state.isUpsertFolderDialogVisible,
        icon = Icons.Rounded.Add,
        title = stringResource(Res.string.create_folder),
        content = {
            TextField(
                label = { Text(text = stringResource(Res.string.name)) },
                value = state.newFolderName,
                onValueChange = { onAction(FolderListAction.NewFolderName(it)) }
            )
            TextField(
                label = { Text(text = "Max File Size Megabytes") },
                value = state.newFolderMaxSize.toString(),
                onValueChange = { input ->
                    val filteredInput = input.filter { it.isDigit() }.toInt()
                    onAction(FolderListAction.NewFolderMaxSize(filteredInput))
                }
            )
        },
        isCrucialAction = false,
        confirmButton = {
            onAction(FolderListAction.UpsertFolder)
            onAction(FolderListAction.DismissUpsertFolderDialog)
        },
        dismissButton = { onAction(FolderListAction.DismissUpsertFolderDialog) }
    )
}

@Composable
private fun Content(
    navigationState: NavigationState,
    state: FolderListState,
    onAction: (FolderListAction) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 15.dp, end = 15.dp),
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SimpleTextFieldSearchBar(
                modifier = Modifier
                    .weight(1f),
                leadingIcon = { Icon(Icons.Rounded.Search, null) },
                placeholder = stringResource(Res.string.search),
                query = state.searchQuery,
                onQueryChange = { onAction(FolderListAction.SearchQuery(it)) }
            )
            FilledIconButton(
                onClick = { onAction(FolderListAction.ShowUpsertFolderDialog) },
                content = { Icon(Icons.Rounded.CreateNewFolder, null) }
            )
        }
        LazyColumn(
            modifier = Modifier
                .weight(1f),
            contentPadding = PaddingValues(start = 15.dp, end = 15.dp, bottom = 100.dp),
            verticalArrangement = Arrangement.spacedBy(ListItemDefaults.SegmentedGap)
        ) {
            if (state.isFolderListLoading) {
                item("isFolderListLoading") {
                    CenterContainedLoadingIndicator(
                        modifier = Modifier
                            .animateItem()
                    )
                }
                return@LazyColumn
            }
            if (state.isFolderListError != null) {
                item("isFolderListError") {
                    MessageCard(
                        modifier = Modifier,
                        icon = Icons.Rounded.Warning,
                        title = stringResource(Res.string.error),
                        message = state.isFolderListError
                    )
                }
                return@LazyColumn
            }
            if (state.folderList.isEmpty()) {
                item("isFolderListEmpty") {
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
                items = state.folderList,
                key = { _, folderModel -> folderModel.name }
            ) { index, folderModel ->
                val targetNavigation = NavigationKey.FileListScreen(
                    folderName = folderModel.name
                )
                SegmentedListItem(
                    modifier = Modifier
                        .animateItem(),
                    onClick = { navigationState.navigateToDetail(targetNavigation) },
                    colors = ListItemDefaults.segmentedColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                    ),
                    shapes = ListItemDefaults.segmentedShapes(index, state.folderList.size),
                    selected = navigationState.backStack.last() == targetNavigation,
                    leadingContent = { Icon(Icons.Rounded.Folder, null) },
                    content = {
                        Text(
                            text = folderModel.name,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    trailingContent = {
                        Text(
                            text = "${folderModel.maxFileSizeMegabyte} MB Max / File"
                        )
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
        state = FolderListState(
            isFolderListLoading = false
        ),
        onAction = {}
    )
}