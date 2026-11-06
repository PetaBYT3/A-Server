package com.a.server.presentation.database.upserttable

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Key
import androidx.compose.material.icons.rounded.Remove
import androidx.compose.material.icons.rounded.Save
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedListItem
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import aserver.app.shared.generated.resources.Res
import aserver.app.shared.generated.resources.name
import aserver.app.shared.generated.resources.primary
import aserver.app.shared.generated.resources.table_name
import aserver.app.shared.generated.resources.type
import aserver.app.shared.generated.resources.upsert
import com.a.server.core.type.SqlDataType
import com.a.server.navigation.NavigationState
import com.a.server.presentation.ui.TransparentTopAppBar
import com.a.server.presentation.util.EffectLauncher
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun UpsertTableDetailScreen(
    tableId: String,
    navigationState: NavigationState = koinInject(),
    viewModel: UpsertViewModel = koinViewModel(
        parameters = {
            parametersOf(tableId)
        }
    )
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
}

@Composable
private fun Content(
    navigationState: NavigationState,
    state: UpsertState,
    onAction: (UpsertAction) -> Unit,
    snackBarHostState: SnackbarHostState
) {
    Scaffold(
        topBar = {
            TransparentTopAppBar(
                onNavigationClick = { navigationState.navigateBack() },
                title = stringResource(Res.string.upsert)
            )
        },
        content = { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(start = 15.dp, end = 15.dp, bottom = 100.dp),
                verticalArrangement = Arrangement.spacedBy(2.5.dp)
            ) {
                item("tableName") {
                    TextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .animateItem(),
                        label = { Text(text = stringResource(Res.string.table_name)) },
                        value = state.tableWithFields.table.name,
                        onValueChange = { onAction(UpsertAction.TableName(it)) }
                    )
                }
                item("spacerTableColumn") {
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(10.dp)
                            .animateItem()
                    )
                }
                itemsIndexed(
                    items = state.tableWithFields.fields,
                    key = { index, _ -> index }
                ) { index, fieldModel ->
                    SegmentedListItem(
                        modifier = Modifier
                            .animateItem(),
                        colors = ListItemDefaults.segmentedColors(
                            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                        ),
                        shapes = ListItemDefaults.segmentedShapes(
                            index = index,
                            count = state.tableWithFields.fields.size
                        ),
                        leadingContent = {
                            Checkbox(
                                checked = fieldModel.primary,
                                onCheckedChange = {
                                    onAction(UpsertAction.PrimaryKey(fieldModel.id))
                                }
                            )
                        },
                        overlineContent = {
                            AnimatedVisibility(fieldModel.primary) {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(Icons.Rounded.Key, null)
                                    Text(text = stringResource(Res.string.primary))
                                }
                            }
                        },
                        content = {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                OutlinedTextField(
                                    modifier = Modifier
                                        .width(150.dp),
                                    label = { Text(text = stringResource(Res.string.name)) },
                                    value = fieldModel.name,
                                    onValueChange = {
                                        onAction(UpsertAction.FieldName(fieldModel.id, it))
                                    }
                                )
                                OutlinedTextField(
                                    modifier = Modifier
                                        .width(150.dp),
                                    label = { Text(text = stringResource(Res.string.type)) },
                                    value = fieldModel.type.name,
                                    onValueChange = {
                                        onAction(UpsertAction.FieldType(fieldModel.id, SqlDataType.valueOf(it))) }
                                )
                            }
                        },
                        trailingContent = {
                            IconButton(
                                onClick = { onAction(UpsertAction.RemoveField(fieldModel.id)) },
                                content = { Icon(Icons.Rounded.Remove, null) }
                            )
                        }
                    )
                }
                item("addFields") {
                    FilledTonalButton(
                        modifier = Modifier
                            .height(ButtonDefaults.MediumContainerHeight)
                            .fillMaxWidth()
                            .padding(top = 12.5.dp)
                            .animateItem(),
                        onClick = { onAction(UpsertAction.AddField) },
                        content = { Text(text = "Add Fields") }
                    )
                }
            }
        },
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onAction(UpsertAction.Upsert) },
                content = { Icon(Icons.Rounded.Save, null) }
            )
        }
    )
}

@Composable
@Preview
private fun Preview() {
    Content(
        navigationState = NavigationState(),
        state = UpsertState(),
        onAction = {},
        snackBarHostState = SnackbarHostState()
    )
}