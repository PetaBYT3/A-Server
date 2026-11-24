@file:OptIn(ExperimentalMaterial3Api::class)

package com.a.server.presentation.database.upsertrow

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import aserver.app.shared.generated.resources.Res
import aserver.app.shared.generated.resources.delete
import aserver.app.shared.generated.resources.delete_row
import aserver.app.shared.generated.resources.row_delete_confirm
import aserver.app.shared.generated.resources.upsert
import com.a.server.core.type.DataType
import com.a.server.domain.model.sqlite.CellModel
import com.a.server.navigation.NavigationState
import com.a.server.presentation.ui.SimpleAlertDialog
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun DatabaseUpsertRowScreen(
    tableName: String,
    rowId: String,
    navigationState: NavigationState = koinInject(),
    viewModel: UpsertViewModel = koinViewModel(
        parameters = {
            parametersOf(tableName, rowId)
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

    SimpleAlertDialog(
        visible = state.isDeleteDialogVisible,
        icon = Icons.Rounded.Delete,
        title = stringResource(Res.string.delete_row),
        content = { Text(text = stringResource(Res.string.row_delete_confirm)) },
        isCrucialAction = true,
        confirmButton = {
            viewModel.onAction(UpsertAction.Delete)
            viewModel.onAction(UpsertAction.DeleteDialog)
        },
        dismissButton = { viewModel.onAction(UpsertAction.DeleteDialog) }
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
                .fillMaxWidth()
                .height(56.dp)
                .padding(start = 15.dp, end = 15.dp),
            horizontalArrangement = Arrangement.spacedBy(15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(
                modifier = Modifier
                    .weight(1f)
            )
            if (state.isOnEdit) {
                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error,
                        contentColor = MaterialTheme.colorScheme.onError
                    ),
                    onClick = { onAction(UpsertAction.DeleteDialog) },
                    content = { Text(text = stringResource(Res.string.delete)) }
                )
            }
            Button(
                onClick = { onAction(UpsertAction.Upsert) },
                content = { Text(text = stringResource(Res.string.upsert)) }
            )
        }
        LazyColumn(
            modifier = Modifier
                .weight(1f),
            contentPadding = PaddingValues(start = 15.dp, end = 15.dp, bottom = 100.dp),
            verticalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            item("rowData") {
                Card(
                    modifier = Modifier
                        .animateItem(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .padding(15.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        state.cells.fastForEach { cellModel ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(
                                    modifier = Modifier
                                        .weight(3f)
                                ) {
                                    Text(
                                        text = cellModel.columnName,
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                    Text(
                                        text = cellModel.type.name,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                OutlinedTextField(
                                    modifier = Modifier
                                        .weight(7f),
                                    value = cellModel.value.toString(),
                                    onValueChange = {
                                        val targetAction = UpsertAction.CellValue(
                                            columnName = cellModel.columnName,
                                            newValue = it
                                        )
                                        onAction(targetAction)
                                    },
                                    singleLine = true
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
@Preview
private fun Preview() {
    Content(
        navigationState = NavigationState(),
        state = UpsertState(
            cells = listOf(
                CellModel(
                    isPrimary = true,
                    columnName = "id",
                    type = DataType.TEXT,
                    value = ""
                ),
                CellModel(
                    isPrimary = false,
                    columnName = "name",
                    type = DataType.TEXT,
                    value = ""
                )
            )
        ),
        onAction = {},
        snackBarHostState = SnackbarHostState()
    )
}