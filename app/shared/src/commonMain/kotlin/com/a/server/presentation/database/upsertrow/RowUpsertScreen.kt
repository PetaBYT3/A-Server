package com.a.server.presentation.database.upsertrow

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import aserver.app.shared.generated.resources.Res
import aserver.app.shared.generated.resources.upsert
import com.a.server.core.type.SqlDataType
import com.a.server.domain.model.sqlite.CellModel
import com.a.server.navigation.database.DatabaseNavigator
import com.a.server.presentation.ui.TransparentTopAppBar
import com.a.server.presentation.util.EffectLauncher
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun UpsertRowScreen(
    tableName: String,
    rowId: String,
    navigator: DatabaseNavigator = koinInject(),
    viewModel: RowUpsertViewModel = koinViewModel(
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

    RowUpsertCompact(
        navigator = navigator,
        state = state,
        onAction = viewModel::onAction,
        snackBarHostState = snackBarHostState
    )
}

@Composable
fun RowUpsertCompact(
    navigator: DatabaseNavigator,
    state: RowUpsertState,
    onAction: (RowUpsertAction) -> Unit,
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
                verticalArrangement = Arrangement.spacedBy(15.dp)
            ) {
                itemsIndexed(
                    items = state.cells,
                    key = { _, cellModel -> cellModel.columnName }
                ) { index, cellModel ->
                    TextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .animateItem(),
                        label = { Text(text = cellModel.columnName) },
                        value = cellModel.value.toString(),
                        onValueChange = {
                            val targetAction = RowUpsertAction.CellValue(cellModel.columnName, it)
                            onAction(targetAction)
                        }
                    )
                }
            }
        },
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
        floatingActionButton = {
            FilledTonalButton(
                onClick = { onAction(RowUpsertAction.Upsert) },
                content = { Text(text = stringResource(Res.string.upsert)) }
            )
        }
    )
}

@Composable
@Preview
private fun Preview() {
    RowUpsertCompact(
        navigator = DatabaseNavigator(),
        state = RowUpsertState(
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