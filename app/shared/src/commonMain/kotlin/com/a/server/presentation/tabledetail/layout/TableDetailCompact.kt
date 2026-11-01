package com.a.server.presentation.tabledetail.layout

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import aserver.app.shared.generated.resources.Res
import aserver.app.shared.generated.resources.detail
import com.a.server.navigation.Navigator
import com.a.server.presentation.tabledetail.TableDetailAction
import com.a.server.presentation.tabledetail.TableDetailState
import com.a.server.presentation.ui.TransparentTopAppBar
import org.jetbrains.compose.resources.stringResource

@Composable
fun TableDetailCompact(
    tableId: String,
    navigator: Navigator,
    state: TableDetailState,
    onAction: (TableDetailAction) -> Unit,
    snackBarHostState: SnackbarHostState
) {
    val horizontalScrollState = rememberScrollState()
    val verticalListState = rememberLazyListState()

    Scaffold(
        topBar = {
            TransparentTopAppBar(
                onNavigationClick = { navigator.navigateBack() },
                title = stringResource(Res.string.detail)
            )
        },
        content = { innerPadding ->
            val tableRows = listOf(
                listOf("ID", "Name", "Email", "Role", "Status", "Created At"),
                listOf("001", "Andrea", "andrea@email.com", "Developer", "Active", "Nov 01 2026"),
                listOf("002", "Hussanini", "hussanini@email.com", "Admin", "Active", "Nov 02 2026")
            )

            Box(
                modifier = Modifier
                    .padding(innerPadding)
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize(),
                    state = verticalListState,
                    contentPadding = PaddingValues(start = 15.dp, end = 15.dp, bottom = 100.dp),
                    verticalArrangement = Arrangement.spacedBy(2.5.dp)
                ) {
                    items(tableRows) { rowData ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(horizontalScrollState),
                            horizontalArrangement = Arrangement.spacedBy(2.5.dp)
                        ) {
                            rowData.fastForEach { cellText ->
                                Card(
                                    modifier = Modifier
                                        .width(250.dp)
                                        .height(50.dp)
                                ) {
                                    Box(
                                        modifier = Modifier.fillMaxSize(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(text = cellText, maxLines = 1)
                                    }
                                }
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
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) }
    )
}

@Composable
@Preview
private fun Preview() {
    TableDetailCompact(
        tableId = "",
        navigator = Navigator(),
        state = TableDetailState(),
        onAction = {},
        snackBarHostState = SnackbarHostState()
    )
}