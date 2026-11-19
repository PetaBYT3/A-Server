package com.a.server.presentation.database.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedListItem
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import aserver.app.shared.generated.resources.Res
import aserver.app.shared.generated.resources.empty
import aserver.app.shared.generated.resources.error
import aserver.app.shared.generated.resources.search
import aserver.app.shared.generated.resources.table_24
import com.a.server.navigation.EffectLauncher
import com.a.server.navigation.NavigationKey
import com.a.server.navigation.NavigationState
import com.a.server.presentation.ui.CenterContainedLoadingIndicator
import com.a.server.presentation.ui.MessageCard
import com.a.server.presentation.ui.SimpleTextFieldSearchBar
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun DatabaseListScreen(
    navigationState: NavigationState = koinInject(),
    viewModel: ListViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackBarHostState = remember { SnackbarHostState() }

    EffectLauncher(
        snackBarHostState = snackBarHostState,
        screenNavigationEffect = viewModel.effect
    )

    Content(
        navigationState = navigationState,
        state = state,
        onAction = viewModel::onAction,
        snackBarHostState = snackBarHostState
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
        Row(
            modifier = Modifier
                .padding(start = 15.dp, end = 15.dp),
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SimpleTextFieldSearchBar(
                modifier = Modifier
                    .weight(1f),
                placeholder = stringResource(Res.string.search),
                leadingIcon = { Icon(Icons.Rounded.Search, null) },
                query = state.searchKey,
                onQueryChange = { onAction(ListAction.SearchKey(it)) }
            )
            FilledIconButton(
                onClick = { navigationState.navigateToDetail(NavigationKey.DatabaseUpsertTable("")) },
                content = { Icon(Icons.Rounded.Add, null) }
            )
        }
        LazyColumn(
            modifier = Modifier
                .weight(1f),
            contentPadding = PaddingValues(start = 15.dp, end = 15.dp, bottom = 100.dp),
            verticalArrangement = Arrangement.spacedBy(ListItemDefaults.SegmentedGap)
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
                val targetNavigationKey = NavigationKey.DatabaseDetail(tableModel.table.name)
                SegmentedListItem(
                    modifier = Modifier
                        .animateItem(),
                    colors = ListItemDefaults.segmentedColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                    ),
                    shapes = ListItemDefaults.segmentedShapes(index, state.filteredTables.size),
                    selected = navigationState.backStack.lastOrNull() == targetNavigationKey,
                    onClick = {
                        navigationState.navigateToDetail(targetNavigationKey)
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
        navigationState = NavigationState(),
        state = ListState(
            isTablesLoading = false
        ),
        onAction = {},
        snackBarHostState = SnackbarHostState()
    )
}