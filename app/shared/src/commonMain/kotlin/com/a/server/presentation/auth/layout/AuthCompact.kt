package com.a.server.presentation.auth.layout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedListItem
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import aserver.app.shared.generated.resources.Res
import aserver.app.shared.generated.resources.authentication
import aserver.app.shared.generated.resources.delete
import aserver.app.shared.generated.resources.empty
import aserver.app.shared.generated.resources.error
import aserver.app.shared.generated.resources.users_empty
import com.a.server.navigation.Navigator
import com.a.server.navigation.Route
import com.a.server.presentation.auth.AuthAction
import com.a.server.presentation.auth.AuthState
import com.a.server.presentation.ui.CenterContainedLoadingIndicator
import com.a.server.presentation.ui.MessageCard
import com.a.server.presentation.ui.SimpleTextFieldSearchBar
import com.a.server.presentation.ui.SlideUpAnimatedVisibility
import com.a.server.presentation.ui.TransparentTopAppBar
import org.jetbrains.compose.resources.stringResource

@Composable
fun AuthCompact(
    navigator: Navigator,
    state: AuthState,
    onAction: (AuthAction) -> Unit,
    snackBarHostState: SnackbarHostState
) {
    Scaffold(
        topBar = {
            TransparentTopAppBar(
                onNavigationClick = { navigator.navigateBack() },
                title = stringResource(Res.string.authentication)
            )
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding),
                verticalArrangement = Arrangement.spacedBy(15.dp)
            ) {
                SimpleTextFieldSearchBar(
                    modifier = Modifier
                        .padding(horizontal = 15.dp),
                    placeholder = "Search here...",
                    query = state.search,
                    onQueryChange = { onAction(AuthAction.Search(it)) }
                )
                LazyColumn(
                    modifier = Modifier
                        .weight(1f),
                    contentPadding = PaddingValues(start = 15.dp, end = 15.dp, bottom = 100.dp),
                    verticalArrangement = Arrangement.spacedBy(2.5.dp)
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
                            shapes = ListItemDefaults.segmentedShapes(index, state.filteredAuthenticationList.size),
                            onClick = {
                                navigator.navigateToDetailOrExtra(Route.AuthDetailScreen(authenticationUi.id))
                            },
                            leadingContent = {
                                Checkbox(
                                    checked = authenticationUi.id in state.selectedAuthenticationId,
                                    onCheckedChange = { onAction(AuthAction.Select(authenticationUi.id)) }
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
        },
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
        floatingActionButton = {
            SlideUpAnimatedVisibility(
                visible = state.selectedAuthenticationId.isNotEmpty()
            ) {
                ExtendedFloatingActionButton(
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                    contentColor = MaterialTheme.colorScheme.onErrorContainer,
                    onClick = { onAction(AuthAction.DeleteAlertDialog) },
                    icon = { Icon(Icons.Rounded.Delete, null) },
                    text = { Text(text = stringResource(Res.string.delete)) },
                )
            }
        }
    )
}

@Composable
@Preview
private fun Preview() {
    AuthCompact(
        navigator = Navigator(),
        state = AuthState(),
        onAction = {},
        snackBarHostState = SnackbarHostState()
    )
}