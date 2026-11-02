package com.a.server.presentation.table.layout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Terminal
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import aserver.app.shared.generated.resources.add
import aserver.app.shared.generated.resources.empty
import aserver.app.shared.generated.resources.error
import aserver.app.shared.generated.resources.search
import aserver.app.shared.generated.resources.table
import aserver.app.shared.generated.resources.table_24
import com.a.server.navigation.Navigator
import com.a.server.navigation.Route
import com.a.server.presentation.table.TableAction
import com.a.server.presentation.table.TableState
import com.a.server.presentation.ui.CenterContainedLoadingIndicator
import com.a.server.presentation.ui.MessageCard
import com.a.server.presentation.ui.SimpleTextFieldSearchBar
import com.a.server.presentation.ui.TransparentTopAppBar
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

@Composable
fun TableCompact(
    navigator: Navigator,
    state: TableState,
    onAction: (TableAction) -> Unit,
    snackBarHostState: SnackbarHostState
) {
    Scaffold(
        topBar = {
            TransparentTopAppBar(
                onNavigationClick = { navigator.navigateBack() },
                title = stringResource(Res.string.table),
                actions = {
                    IconButton(
                        onClick = { navigator.navigateToDetailOrExtra(Route.TableSqlScreen) },
                        content = { Icon(Icons.Rounded.Terminal, null) }
                    )
                }
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
                        .padding(start = 15.dp, end = 15.dp),
                    placeholder = stringResource(Res.string.search),
                    leadingIcon = { Icon(Icons.Rounded.Search, null) },
                    query = state.searchKey,
                    onQueryChange = { onAction(TableAction.SearchKey(it)) }
                )
                LazyColumn(
                    modifier = Modifier
                        .weight(1f),
                    contentPadding = PaddingValues(start = 15.dp, end = 15.dp, bottom = 100.dp),
                    verticalArrangement = Arrangement.spacedBy(2.5.dp)
                ) {
                    if (state.isTablesLoading) {
                        item("isTableListLoading") {
                            CenterContainedLoadingIndicator(
                                modifier = Modifier
                                    .animateItem()
                            )
                        }
                        return@LazyColumn
                    }
                    if (state.isTablesError != null) {
                        item("isTableListError") {
                            MessageCard(
                                modifier = Modifier
                                    .animateItem(),
                                icon = Icons.Rounded.Warning,
                                title = stringResource(Res.string.error),
                                message = state.isTablesError
                            )
                        }
                        return@LazyColumn
                    }
                    if (state.filteredTables.isEmpty()) {
                        item("empty") {
                            MessageCard(
                                modifier = Modifier
                                    .animateItem(),
                                icon = Icons.Rounded.Warning,
                                title = stringResource(Res.string.empty),
                                message = stringResource(Res.string.empty)
                            )
                        }
                        return@LazyColumn
                    }
                    itemsIndexed(
                        items = state.filteredTables,
                        key = { _, tableUi -> tableUi }
                    ) { index, tableModel ->
                        SegmentedListItem(
                            modifier = Modifier
                                .animateItem(),
                            colors = ListItemDefaults.segmentedColors(
                                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                            ),
                            shapes = ListItemDefaults.segmentedShapes(index, state.filteredTables.size),
                            onClick = {
                                val targetRoute = Route.TableDetailScreen(tableModel.table.name)
                                navigator.navigateToDetailOrExtra(targetRoute)
                            },
                            leadingContent = {
                                Icon(vectorResource(Res.drawable.table_24), null)
                            },
                            content = {
                                Text(
                                    text = tableModel.table.name,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            },
                            supportingContent = { Text(text = "Total Column: ${tableModel.fields.size}") }
                        )
                    }
                }
            }
        },
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { navigator.navigateToDetailOrExtra(Route.TableUpsertScreen("")) },
                icon = { Icon(Icons.Rounded.Add, null) },
                text = { Text(text = stringResource(Res.string.add)) }
            )
        }
    )
}

@Composable
@Preview
private fun Preview() {
    TableCompact(
        navigator = Navigator(),
        state = TableState(
            isTablesLoading = false
        ),
        onAction = {},
        snackBarHostState = SnackbarHostState()
    )
}