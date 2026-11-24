package com.a.server.presentation.storage.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.InsertDriveFile
import androidx.compose.material.icons.rounded.QuestionMark
import androidx.compose.material.icons.rounded.Tag
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import aserver.app.shared.generated.resources.Res
import aserver.app.shared.generated.resources.empty
import aserver.app.shared.generated.resources.error
import aserver.app.shared.generated.resources.file_name
import aserver.app.shared.generated.resources.file_path
import aserver.app.shared.generated.resources.file_size
import com.a.server.navigation.NavigationState
import com.a.server.presentation.authentication.detail.component.DetailRow
import com.a.server.presentation.ui.CenterContainedLoadingIndicator
import com.a.server.presentation.ui.MessageCard
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun StorageDetailScreen(
    folderName: String,
    navigationState: NavigationState = koinInject(),
    viewModel: DetailViewModel = koinViewModel(
        parameters = {
            parametersOf(folderName)
        }
    )
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Content(
        navigationState = navigationState,
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
private fun Content(
    navigationState: NavigationState,
    state: DetailState,
    onAction: (DetailAction) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        Row(
            modifier = Modifier
                .height(56.dp)
                .fillMaxWidth()
        ) {

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
                    colors = ListItemDefaults.segmentedColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                    ),
                    shapes = ListItemDefaults.segmentedShapes(index, state.fileList.size),
                    leadingContent = { Icon(Icons.Rounded.InsertDriveFile, null) },
                    content = { Text(text = fileModel.name) },
                    supportingContent = { Text(text = fileModel.size) }
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
        state = DetailState(),
        onAction = {}
    )
}