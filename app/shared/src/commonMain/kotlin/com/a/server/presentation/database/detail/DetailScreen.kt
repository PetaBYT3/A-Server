package com.a.server.presentation.database.detail

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import aserver.app.shared.generated.resources.Res
import aserver.app.shared.generated.resources.delete_row
import aserver.app.shared.generated.resources.delete_table
import aserver.app.shared.generated.resources.empty
import aserver.app.shared.generated.resources.error
import aserver.app.shared.generated.resources.row_delete_confirm
import aserver.app.shared.generated.resources.table_24
import aserver.app.shared.generated.resources.table_delete_confirm
import aserver.app.shared.generated.resources.upsert_row
import com.a.server.core.type.SqlDataType
import com.a.server.domain.model.sqlite.CellModel
import com.a.server.domain.model.sqlite.FieldModel
import com.a.server.domain.model.sqlite.RowModel
import com.a.server.domain.model.sqlite.TableCompleteModel
import com.a.server.domain.model.sqlite.TableModel
import com.a.server.domain.model.sqlite.TableWithFieldsModel
import com.a.server.navigation.NavigationKey
import com.a.server.navigation.NavigationState
import com.a.server.presentation.database.detail.component.Cell
import com.a.server.presentation.model.IconUi
import com.a.server.presentation.ui.CenterContainedLoadingIndicator
import com.a.server.presentation.ui.DirectionalAnimatedVisibility
import com.a.server.presentation.ui.GenericStandardDropDownMenu
import com.a.server.presentation.ui.MessageCard
import com.a.server.presentation.ui.SimpleAlertDialog
import com.a.server.presentation.ui.UniversalHorizontalScrollStateScrollbar
import com.a.server.presentation.ui.VisibilityDirection
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun DatabaseDetailScreen(
    tableName: String,
    navigationState: NavigationState = koinInject(),
    viewModel: DetailViewModel = koinViewModel(
        parameters = {
            parametersOf(tableName)
        }
    )
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Content(
        tableName = tableName,
        navigationState = navigationState,
        state = state,
        onAction = viewModel::onAction
    )

    SimpleAlertDialog(
        visible = state.isDeleteTableDialogVisible,
        onDismissRequest = { viewModel.onAction(DetailAction.DeleteTableDialog) },
        icon = Icons.Rounded.Delete,
        title = stringResource(Res.string.delete_table),
        content = { Text(text = stringResource(Res.string.table_delete_confirm)) },
        isCrucialAction = true,
        confirmButton = {
            viewModel.onAction(DetailAction.DeleteTable)
            viewModel.onAction(DetailAction.DeleteTableDialog)
        },
        dismissButton = {
            viewModel.onAction(DetailAction.DeleteTableDialog)
        }
    )

    SimpleAlertDialog(
        visible = state.isDeleteRowsDialogVisible,
        onDismissRequest = { viewModel.onAction(DetailAction.DeleteRowDialog) },
        icon = Icons.Rounded.Delete,
        title = stringResource(Res.string.delete_row),
        content = { Text(text = stringResource(Res.string.row_delete_confirm)) },
        isCrucialAction = true,
        confirmButton = {
            viewModel.onAction(DetailAction.DeleteRow)
            viewModel.onAction(DetailAction.DeleteRowDialog)
        },
        dismissButton = {
            viewModel.onAction(DetailAction.DeleteRowDialog)
        }
    )
}

@Composable
private fun Content(
    tableName: String,
    navigationState: NavigationState,
    state: DetailState,
    onAction: (DetailAction) -> Unit
) {
    val horizontalScrollState = rememberScrollState()
    val verticalListState = rememberLazyListState()

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            Row(
                modifier = Modifier
                    .height(56.dp)
                    .padding(start = 15.dp, end = 15.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(vectorResource(Res.drawable.table_24), null)
                Text(
                    text = tableName,
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(
                    modifier = Modifier
                        .weight(1f)
                )
                DirectionalAnimatedVisibility(
                    visible = state.selectedRows.isNotEmpty(),
                    direction = VisibilityDirection.Start
                ) {
                    Button(
                        modifier = Modifier
                            .padding(end = 5.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error,
                            contentColor = MaterialTheme.colorScheme.onError
                        ),
                        onClick = { onAction(DetailAction.DeleteRowDialog) },
                        content = { Text(text = stringResource(Res.string.delete_row)) }
                    )
                }
                GenericStandardDropDownMenu(
                    content = { show ->
                        FilledIconButton(
                            onClick = { show() },
                            content = { Icon(Icons.Rounded.MoreVert, null) }
                        )
                    },
                    items = listOf<Triple<IconUi, String, () -> Unit>>(
                        Triple(
                            IconUi.Vector(Icons.Rounded.Delete),
                            stringResource(Res.string.delete_table)
                        ) {
                            onAction(DetailAction.DeleteTableDialog)
                        },
                        Triple(
                            IconUi.Vector(Icons.Rounded.Add),
                            stringResource(Res.string.upsert_row)
                        ) {
                            val targetNavigationKey = NavigationKey.DatabaseUpsertRow(
                                tableName = tableName,
                                rowId = ""
                            )
                            navigationState.navigateTo(targetNavigationKey)
                        }
                    ),
                    itemContent = { item, dismiss ->
                        DropdownMenuItem(
                            onClick = {
                                item.third()
                                dismiss()
                            },
                            leadingIcon = { Icon(item.first.asIcon(), null) },
                            text = { Text(text = item.second) }
                        )
                    }
                )
            }
            LazyColumn(
                modifier = Modifier
                    .weight(1f),
                state = verticalListState,
                contentPadding = PaddingValues(start = 15.dp, end = 15.dp, bottom = 100.dp),
                verticalArrangement = Arrangement.spacedBy(ListItemDefaults.SegmentedGap)
            ) {
                if (state.isTableCompleteLoading) {
                    item("isTableWithFieldsLoading") {
                        CenterContainedLoadingIndicator(
                            modifier = Modifier
                                .animateItem()
                        )
                    }
                    return@LazyColumn
                }
                if (state.isTableCompleteError != null) {
                    item("isTableWithFieldsError") {
                        MessageCard(
                            modifier = Modifier
                                .animateItem(),
                            icon = Icons.Rounded.Warning,
                            title = stringResource(Res.string.error),
                            message = state.isTableCompleteError
                        )
                    }
                    return@LazyColumn
                }
                if (state.tableComplete == null) {
                    item("isTableWithFieldsError") {
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
                item("tableFields") {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(IntrinsicSize.Min)
                            .horizontalScroll(horizontalScrollState)
                            .animateItem(),
                        horizontalArrangement = Arrangement.spacedBy(ListItemDefaults.SegmentedGap)
                    ) {
                        Cell(
                            modifier = Modifier
                                .fillMaxHeight(),
                            isField = true,
                            content = {
                                Checkbox(
                                    modifier = Modifier
                                        .height(25.dp),
                                    checked = state.tableComplete.rows.isNotEmpty() && state.tableComplete.rows.size == state.selectedRows.size,
                                    onCheckedChange = { checked ->
                                        if (checked) {
                                            onAction(DetailAction.SelectAllRow)
                                        } else {
                                            onAction(DetailAction.UnselectAllRow)
                                        }
                                    }
                                )
                            }
                        )
                        state.tableComplete.tableWithFields.fields.fastForEach { fieldModel ->
                            Cell(
                                modifier = Modifier
                                    .width(100.dp)
                                    .animateItem(),
                                isField = true,
                                content = { Text(text = fieldModel.name) }
                            )
                        }
                    }
                }
                itemsIndexed(
                    items = state.tableComplete.rows
                ) { index, rowModel ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(IntrinsicSize.Min)
                            .horizontalScroll(horizontalScrollState)
                            .animateItem(),
                        horizontalArrangement = Arrangement.spacedBy(ListItemDefaults.SegmentedGap)
                    ) {
                        Cell(
                            modifier = Modifier
                                .fillMaxHeight(),
                            content = {
                                Checkbox(
                                    modifier = Modifier
                                        .height(25.dp),
                                    checked = rowModel in state.selectedRows,
                                    onCheckedChange = { onAction(DetailAction.SelectRow(rowModel)) }
                                )
                            }
                        )
                        rowModel.cells.fastForEach { cellModel ->
                            Cell(
                                modifier = Modifier
                                    .width(100.dp)
                                    .animateItem(),
                                onClick = {
                                    val targetNavigationKey = NavigationKey.DatabaseUpsertRow(
                                        tableName = tableName,
                                        rowId = rowModel.cells[0].value ?: ""
                                    )
                                    navigationState.navigateTo(targetNavigationKey)
                                },
                                content = { Text(text = cellModel.value.toString()) }
                            )
                        }
                    }
                }
            }
        }

        UniversalHorizontalScrollStateScrollbar(
            modifier = Modifier
                .padding(start = 15.dp, end = 15.dp)
                .align(Alignment.BottomCenter),
            scrollState = horizontalScrollState,
        )
    }
}

@Composable
@Preview
private fun Preview() {
    Content(
        tableName = "Table Name",
        navigationState = NavigationState(),
        state = DetailState(
            isTableCompleteLoading = false,
            tableComplete = TableCompleteModel(
                tableWithFields = TableWithFieldsModel(
                    table = TableModel("Preview Table"),
                    fields = listOf(
                        FieldModel(
                            id = "",
                            tableName = "id",
                            primary = true,
                            name = "id",
                            type = SqlDataType.TEXT,
                            defaultValue = ""
                        ),
                        FieldModel(
                            id = "",
                            tableName = "",
                            primary = false,
                            name = "name",
                            type = SqlDataType.TEXT,
                            defaultValue = ""
                        )
                    )
                ),
                rows = listOf(
                    RowModel(
                        cells = listOf(
                            CellModel(
                                isPrimary = true,
                                columnName = "",
                                type = SqlDataType.TEXT,
                                value = "1231K23M12"
                            ),
                            CellModel(
                                isPrimary = false,
                                columnName = "",
                                type = SqlDataType.TEXT,
                                value = "Alucard"
                            )
                        )
                    )
                )
            )
        ),
        onAction = {}
    )
}