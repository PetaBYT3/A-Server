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
import androidx.compose.material3.Button
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
import aserver.app.shared.generated.resources.upsert
import com.a.server.core.type.SqlDataType
import com.a.server.domain.model.sqlite.CellModel
import com.a.server.navigation.NavigationState
import com.a.server.presentation.util.EffectLauncher
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
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(
                modifier = Modifier
                    .weight(1f)
            )
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
                    columnName = "id",
                    type = SqlDataType.TEXT,
                    value = ""
                ),
                CellModel(
                    columnName = "name",
                    type = SqlDataType.TEXT,
                    value = ""
                )
            )
        ),
        onAction = {},
        snackBarHostState = SnackbarHostState()
    )
}