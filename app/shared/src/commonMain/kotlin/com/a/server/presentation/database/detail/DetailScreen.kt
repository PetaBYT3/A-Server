package com.a.server.presentation.database.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import aserver.app.shared.generated.resources.Res
import aserver.app.shared.generated.resources.empty
import aserver.app.shared.generated.resources.error
import com.a.server.core.type.SqlDataType
import com.a.server.domain.model.sqlite.CellModel
import com.a.server.domain.model.sqlite.FieldModel
import com.a.server.domain.model.sqlite.RowModel
import com.a.server.domain.model.sqlite.TableCompleteModel
import com.a.server.domain.model.sqlite.TableModel
import com.a.server.domain.model.sqlite.TableWithFieldsModel
import com.a.server.navigation.database.DatabaseKey
import com.a.server.navigation.database.DatabaseNavigator
import com.a.server.presentation.database.component.Cell
import com.a.server.presentation.ui.CenterContainedLoadingIndicator
import com.a.server.presentation.ui.MessageCard
import com.a.server.presentation.ui.TransparentTopAppBar
import com.a.server.presentation.util.EffectLauncher
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun DetailScreen(
    tableName: String,
    navigator: DatabaseNavigator = koinInject(),
    viewModel: DetailViewModel = koinViewModel(
        parameters = {
            parametersOf(tableName)
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
        tableName = tableName,
        navigator = navigator,
        state = state,
        onAction = viewModel::onAction,
        snackBarHostState = snackBarHostState
    )
}

@Composable
fun Content(
    tableName: String,
    navigator: DatabaseNavigator,
    state: DetailState,
    onAction: (DetailAction) -> Unit,
    snackBarHostState: SnackbarHostState
) {
    val horizontalScrollState = rememberLazyListState()
    val verticalListState = rememberLazyListState()

    Scaffold(
        topBar = {
            TransparentTopAppBar(
                onNavigationClick = { navigator.navigateBack() },
                title = tableName
            )
        },
        content = { innerPadding ->
            Box(
                modifier = Modifier
                    .padding(innerPadding)
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize(),
                    state = verticalListState,
                    contentPadding = PaddingValues(bottom = 100.dp),
                    verticalArrangement = Arrangement.spacedBy(2.5.dp)
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
                        LazyRow(
                            modifier = Modifier
                                .fillMaxWidth(),
                            state = horizontalScrollState,
                            contentPadding = PaddingValues(start = 15.dp, end = 15.dp),
                            horizontalArrangement = Arrangement.spacedBy(2.5.dp)
                        ) {
                            itemsIndexed(
                                items = state.tableComplete.tableWithFields.fields
                            ) { index, fieldModel ->
                                Cell(
                                    modifier = Modifier
                                        .animateItem(),
                                    isField = true,
                                    value = fieldModel.name
                                )
                            }
                        }
                    }
                    itemsIndexed(
                        items = state.tableComplete.rows
                    ) { index, rowModel ->
                        LazyRow(
                            modifier = Modifier
                                .fillMaxWidth(),
                            contentPadding = PaddingValues(start = 15.dp, end = 15.dp),
                            horizontalArrangement = Arrangement.spacedBy(2.5.dp)
                        ) {
                            itemsIndexed(
                                items = rowModel.cells
                            ) { index, cellModel ->
                                Cell(
                                    modifier = Modifier
                                        .animateItem(),
                                    value = cellModel.value.toString()
                                )
                            }
                        }
                    }
                }

//                VerticalScrollbar(
//                    modifier = Modifier
//                        .align(Alignment.CenterEnd)
//                        .fillMaxHeight()
//                        .padding(start = 5.dp, end = 5.dp),
//                    adapter = rememberScrollbarAdapter(verticalListState),
//                    style = ScrollbarStyle(
//                        minimalHeight = 16.dp,
//                        thickness = 8.dp,
//                        shape = CircleShape,
//                        hoverDurationMillis = 300,
//                        unhoverColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
//                        hoverColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
//                    )
//                )
//
//                HorizontalScrollbar(
//                    modifier = Modifier
//                        .align(Alignment.BottomCenter)
//                        .fillMaxWidth()
//                        .padding(top = 5.dp, bottom = 5.dp, start = 15.dp, end = 15.dp),
//                    adapter = rememberScrollbarAdapter(horizontalScrollState),
//                    style = ScrollbarStyle(
//                        minimalHeight = 16.dp,
//                        thickness = 8.dp,
//                        shape = CircleShape,
//                        hoverDurationMillis = 300,
//                        unhoverColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
//                        hoverColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
//                    )
//                )
            }
        },
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
        floatingActionButton = {
            FilledTonalButton(
                onClick = {
                    val targetRoute = DatabaseKey.UpsertRow(
                        tableName = tableName,
                        rowId = ""
                    )
                    navigator.navigateTo(targetRoute)
                },
                content = { Text(text = "Upsert Row") }
            )
        }
    )
}

@Composable
@Preview
private fun Preview() {
    Content(
        tableName = "Table Name",
        navigator = DatabaseNavigator(),
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
                                columnName = "",
                                type = SqlDataType.TEXT,
                                value = "1231K23M12"
                            ),
                            CellModel(
                                columnName = "",
                                type = SqlDataType.TEXT,
                                value = "Alucard"
                            )
                        )
                    )
                )
            )
        ),
        onAction = {},
        snackBarHostState = SnackbarHostState()
    )
}