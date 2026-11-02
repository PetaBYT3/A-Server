package com.a.server.presentation.tablesql.layout

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.plus
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Terminal
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
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
import aserver.app.shared.generated.resources.execute
import aserver.app.shared.generated.resources.sql
import com.a.server.navigation.Navigator
import com.a.server.presentation.tablesql.TableSqlAction
import com.a.server.presentation.tablesql.TableSqlState
import com.a.server.presentation.ui.TransparentTopAppBar
import org.jetbrains.compose.resources.stringResource

@Composable
fun TableSqlCompact(
    navigator: Navigator,
    state: TableSqlState,
    onAction: (TableSqlAction) -> Unit,
    snackBarHostState: SnackbarHostState
) {
    Scaffold(
        topBar = {
            TransparentTopAppBar(
                onNavigationClick = { navigator.navigateBack() },
                title = stringResource(Res.string.sql)
            )
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding + PaddingValues(start = 15.dp, end = 15.dp, bottom = 85.dp)),
            ) {
                TextField(
                    modifier = Modifier
                        .fillMaxSize(),
                    value = state.query,
                    onValueChange = { onAction(TableSqlAction.Query(it)) }
                )
            }
        },
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { onAction(TableSqlAction.Execute) },
                icon = { Icon(Icons.Rounded.Terminal, null) },
                text = { Text(text = stringResource(Res.string.execute)) }
            )
        }
    )
}

@Composable
@Preview
private fun Preview() {
    TableSqlCompact(
        navigator = Navigator(),
        state = TableSqlState(),
        onAction = {},
        snackBarHostState = SnackbarHostState()
    )
}