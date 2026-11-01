package com.a.server.presentation.tableupsert.layout

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import aserver.app.shared.generated.resources.Res
import aserver.app.shared.generated.resources.name
import aserver.app.shared.generated.resources.primary
import aserver.app.shared.generated.resources.table_name
import aserver.app.shared.generated.resources.type
import aserver.app.shared.generated.resources.upsert
import com.a.server.domain.model.sqlite.DataType
import com.a.server.navigation.Navigator
import com.a.server.presentation.tableupsert.TableUpsertAction
import com.a.server.presentation.tableupsert.TableUpsertState
import com.a.server.presentation.ui.TransparentTopAppBar
import org.jetbrains.compose.resources.stringResource

@Composable
fun TableUpsertCompact(
    navigator: Navigator,
    state: TableUpsertState,
    onAction: (TableUpsertAction) -> Unit,
    snackBarHostState: SnackbarHostState
) {
    Scaffold(
        topBar = {
            TransparentTopAppBar(
                onNavigationClick = { navigator.navigateBack() },
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
                        value = state.tableName,
                        onValueChange = { onAction(TableUpsertAction.TableName(it)) }
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
                    items = state.fieldList,
                    key = { index, _ -> index }
                ) { index, tableField ->
                    SegmentedListItem(
                        modifier = Modifier
                            .animateItem(),
                        colors = ListItemDefaults.segmentedColors(
                            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                        ),
                        shapes = ListItemDefaults.segmentedShapes(index, state.fieldList.size),
                        leadingContent = {
                            Checkbox(
                                checked = tableField.primary,
                                onCheckedChange = { onAction(TableUpsertAction.PrimaryKey(index)) }
                            )
                        },
                        overlineContent = {
                            AnimatedVisibility(tableField.primary) {
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
                                    value = tableField.name,
                                    onValueChange = { onAction(TableUpsertAction.FieldName(index, it)) }
                                )
                                OutlinedTextField(
                                    modifier = Modifier
                                        .width(150.dp),
                                    label = { Text(text = stringResource(Res.string.type)) },
                                    value = tableField.type.name,
                                    onValueChange = {
                                        onAction(TableUpsertAction.FieldType(index, DataType.valueOf(it))) }
                                )
                            }
                        },
                        trailingContent = {
                            IconButton(
                                onClick = { onAction(TableUpsertAction.RemoveField(index)) },
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
                        onClick = { onAction(TableUpsertAction.AddField) },
                        content = { Text(text = "Add Fields") }
                    )
                }
            }
        },
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onAction(TableUpsertAction.Upsert) },
                content = { Icon(Icons.Rounded.Save, null) }
            )
        }
    )
}

@Composable
@Preview
private fun Preview() {
    TableUpsertCompact(
        navigator = Navigator(),
        state = TableUpsertState(),
        onAction = {},
        snackBarHostState = SnackbarHostState()
    )
}