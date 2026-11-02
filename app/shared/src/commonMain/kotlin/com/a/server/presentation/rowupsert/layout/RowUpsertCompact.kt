package com.a.server.presentation.rowupsert.layout

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import aserver.app.shared.generated.resources.Res
import aserver.app.shared.generated.resources.upsert
import com.a.server.core.type.SqlDataType
import com.a.server.domain.model.sqlite.CellModel
import com.a.server.navigation.Navigator
import com.a.server.presentation.rowupsert.RowUpsertAction
import com.a.server.presentation.rowupsert.RowUpsertState
import com.a.server.presentation.ui.TransparentTopAppBar
import org.jetbrains.compose.resources.stringResource

@Composable
fun RowUpsertCompact(
    navigator: Navigator,
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
        navigator = Navigator(),
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