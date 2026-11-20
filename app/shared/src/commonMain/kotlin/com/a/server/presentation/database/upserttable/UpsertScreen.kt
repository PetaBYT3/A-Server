package com.a.server.presentation.database.upserttable

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedAssistChip
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SegmentedListItem
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import aserver.app.shared.generated.resources.Res
import aserver.app.shared.generated.resources.default_value
import aserver.app.shared.generated.resources.name
import aserver.app.shared.generated.resources.primary
import aserver.app.shared.generated.resources.table_name
import aserver.app.shared.generated.resources.upsert
import com.a.server.core.type.DataType
import com.a.server.navigation.NavigationState
import com.a.server.presentation.ui.GenericExposedDropDownMenu
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun DatabaseUpsertTableScreen(
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
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(start = 15.dp, end = 15.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                    disabledContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                ),
                label = { Text(text = stringResource(Res.string.table_name)) },
                value = state.tableWithFields.table.name,
                onValueChange = { onAction(UpsertAction.TableName(it)) }
            )
            Button(
                modifier = Modifier,
                onClick = { onAction(UpsertAction.Upsert) },
                content = { Text(text = stringResource(Res.string.upsert)) }
            )
        }
        LazyColumn(
            modifier = Modifier
                .weight(1f),
            contentPadding = PaddingValues(start = 15.dp, end = 15.dp, bottom = 100.dp),
            verticalArrangement = Arrangement.spacedBy(ListItemDefaults.SegmentedGap)
        ) {
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
                    overlineContent = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            ElevatedAssistChip(
                                onClick = { onAction(UpsertAction.PrimaryKey(fieldModel.id)) },
                                leadingIcon = {
                                    Checkbox(
                                        checked = fieldModel.primary,
                                        onCheckedChange = null
                                    )
                                },
                                label = { Text(text = stringResource(Res.string.primary)) }
                            )
                            GenericExposedDropDownMenu(
                                modifier = Modifier
                                    .width(175.dp),
                                content = { show ->
                                    ElevatedAssistChip(
                                        modifier = Modifier
                                            .fillMaxWidth(),
                                        onClick = { show() },
                                        label = {
                                            AnimatedContent(
                                                targetState = fieldModel.type,
                                                content = { Text(text = it.name) }
                                            )
                                        },
                                        trailingIcon = { Icon(Icons.Rounded.ArrowDropDown, null) }
                                    )
                                },
                                items = DataType.entries,
                                itemContent = { item, dismiss ->
                                    DropdownMenuItem(
                                        onClick = {
                                            onAction(UpsertAction.FieldType(fieldModel.id, item))
                                            dismiss()
                                        },
                                        leadingIcon = {
                                            if (fieldModel.type == item) {
                                                Icon(Icons.Rounded.Check, null)
                                            }
                                        },
                                        text = { Text(text = item.name) }
                                    )
                                }
                            )
                        }
                    },
                    content = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            OutlinedTextField(
                                modifier = Modifier
                                    .weight(5f),
                                label = { Text(text = stringResource(Res.string.name)) },
                                value = fieldModel.name,
                                singleLine = true,
                                onValueChange = {
                                    onAction(UpsertAction.FieldName(fieldModel.id, it))
                                }
                            )
                            OutlinedTextField(
                                modifier = Modifier
                                    .weight(5f),
                                label = { Text(text = stringResource(Res.string.default_value)) },
                                value = "",
                                singleLine = true,
                                onValueChange = {}
                            )
                        }
                    },
                    trailingContent = {
                        IconButton(
                            colors = IconButtonDefaults.iconButtonColors(
                                contentColor = MaterialTheme.colorScheme.onErrorContainer
                            ),
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
                        .padding(top = 12.5.dp)
                        .animateItem(),
                    onClick = { onAction(UpsertAction.AddField) },
                    content = { Text(text = "Add Fields") }
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
        state = UpsertState(),
        onAction = {},
        snackBarHostState = SnackbarHostState()
    )
}