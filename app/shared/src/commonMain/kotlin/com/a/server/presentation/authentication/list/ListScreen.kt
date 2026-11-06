package com.a.server.presentation.authentication.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedListItem
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import aserver.app.shared.generated.resources.Res
import aserver.app.shared.generated.resources.delete
import aserver.app.shared.generated.resources.empty
import aserver.app.shared.generated.resources.error
import aserver.app.shared.generated.resources.search
import aserver.app.shared.generated.resources.users_delete_confirm
import aserver.app.shared.generated.resources.users_empty
import com.a.server.navigation.NavigationKey
import com.a.server.navigation.NavigationState
import com.a.server.presentation.ui.CenterContainedLoadingIndicator
import com.a.server.presentation.ui.MessageCard
import com.a.server.presentation.ui.SimpleAlertDialog
import com.a.server.presentation.ui.SimpleTextFieldSearchBar
import com.a.server.presentation.util.EffectLauncher
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AuthenticationListScreen(
    navigationState: NavigationState = koinInject(),
    viewModel: ListViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackBarHostState = remember { SnackbarHostState() }

    EffectLauncher(
        snackBarHostState = snackBarHostState,
        screenEffect = viewModel.effect
    )

    Content(
        navigationState = navigationState,
        state = state,
        onAction = viewModel::onAction,
        snackBarHostState = snackBarHostState
    )

    SimpleAlertDialog(
        visible = state.isDeleteDialogVisible,
        onDismissRequest = { viewModel.onAction(ListAction.DeleteAlertDialog) },
        icon = Icons.Rounded.Delete,
        title = stringResource(Res.string.delete),
        text = stringResource(Res.string.users_delete_confirm),
        isCrucialAction = true,
        confirmButton = {
            viewModel.onAction(ListAction.Delete)
            viewModel.onAction(ListAction.DeleteAlertDialog)
        },
        dismissButton = { viewModel.onAction(ListAction.DeleteAlertDialog) }
    )
}

@Composable
private fun Content(
    navigationState: NavigationState,
    state: ListState,
    onAction: (ListAction) -> Unit,
    snackBarHostState: SnackbarHostState
) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        SimpleTextFieldSearchBar(
            modifier = Modifier
                .padding(horizontal = 15.dp),
            leadingIcon = { Icon(Icons.Rounded.Search, null) },
            placeholder = stringResource(Res.string.search),
            query = state.searchKey,
            onQueryChange = { onAction(ListAction.Search(it)) }
        )
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .selectableGroup(),
            contentPadding = PaddingValues(start = 15.dp, end = 15.dp, bottom = 100.dp),
            verticalArrangement = Arrangement.spacedBy(ListItemDefaults.SegmentedGap)
        ) {
            if (state.isAuthenticationListLoading) {
                item("isAuthenticationListLoading") {
                    CenterContainedLoadingIndicator(
                        modifier = Modifier
                            .animateItem()
                    )
                }
                return@LazyColumn
            }
            if (state.isAuthenticationListError != null) {
                item("isAuthenticationListError") {
                    MessageCard(
                        modifier = Modifier
                            .animateItem(),
                        icon = Icons.Rounded.Warning,
                        title = stringResource(Res.string.error),
                        message = state.isAuthenticationListError
                    )
                }
                return@LazyColumn
            }
            if (state.filteredAuthenticationList.isEmpty()) {
                item("authenticationListIsEmpty") {
                    MessageCard(
                        modifier = Modifier
                            .animateItem(),
                        icon = Icons.Rounded.Warning,
                        title = stringResource(Res.string.empty),
                        message = stringResource(Res.string.users_empty)
                    )
                }
                return@LazyColumn
            }
            itemsIndexed(
                items = state.filteredAuthenticationList,
                key = { _, authenticationUi -> authenticationUi.id }
            ) { index, authenticationUi ->
                SegmentedListItem(
                    modifier = Modifier
                        .animateItem(),
                    colors = ListItemDefaults.segmentedColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                    ),
                    shapes = ListItemDefaults.segmentedShapes(
                        index = index,
                        count = state.filteredAuthenticationList.size
                    ),
                    selected = navigationState.backStack.lastOrNull() ==
                            NavigationKey.AuthenticationDetail(authenticationUi.id),
                    onClick = {
                        val targetNavigationKey = NavigationKey.AuthenticationDetail(
                            authId = authenticationUi.id
                        )
                        navigationState.navigateTo(targetNavigationKey)
                    },
                    leadingContent = {
                        Checkbox(
                            checked = authenticationUi.id in state.selectedAuthenticationId,
                            onCheckedChange = { onAction(ListAction.Select(authenticationUi.id)) }
                        )
                    },
                    overlineContent = {
                        Text(
                            text = authenticationUi.id,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    content = {
                        Text(
                            text = authenticationUi.email,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    trailingContent = {
                        Text(
                            text = authenticationUi.createdAt,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                )
            }
        }
    }
}