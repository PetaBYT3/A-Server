package com.a.server.presentation.storage.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.InsertDriveFile
import androidx.compose.material.icons.rounded.Tag
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import aserver.app.shared.generated.resources.Res
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
    targetPath: String,
    navigationState: NavigationState = koinInject(),
    viewModel: DetailViewModel = koinViewModel(
        parameters = {
            parametersOf(targetPath)
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
    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        contentPadding = PaddingValues(start = 15.dp, end = 15.dp, bottom = 100.dp),
        verticalArrangement = Arrangement.spacedBy(15.dp)
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
                    modifier = Modifier,
                    icon = Icons.Rounded.Warning,
                    title = stringResource(Res.string.error),
                    message = state.isFileError
                )
            }
            return@LazyColumn
        }
        item("fileDetail") {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .animateItem(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                )
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        modifier = Modifier
                            .size(125.dp)
                            .padding(top = 10.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        imageVector = Icons.Rounded.InsertDriveFile,
                        contentDescription = null
                    )
                    DetailRow(
                        icon = Icons.Rounded.Tag,
                        title = stringResource(Res.string.file_name),
                        text = state.file?.name ?: ""
                    )
                    DetailRow(
                        icon = Icons.Rounded.Email,
                        title = stringResource(Res.string.file_size),
                        text = state.file?.size ?: ""
                    )
                    DetailRow(
                        icon = Icons.Rounded.Email,
                        title = stringResource(Res.string.file_path),
                        text = state.file?.absolutePath ?: ""
                    )
                }
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