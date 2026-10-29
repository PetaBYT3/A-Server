package com.a.server.presentation.dashboard.layout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import aserver.app.shared.generated.resources.Res
import aserver.app.shared.generated.resources.dashboard
import com.a.server.presentation.dashboard.DashboardAction
import com.a.server.presentation.dashboard.DashboardState
import com.a.server.presentation.dashboard.component.ServerCard
import com.a.server.presentation.dashboard.component.TunnelCard
import org.jetbrains.compose.resources.stringResource

@Composable
fun DashboardExpanded(
    state: DashboardState,
    onAction: (DashboardAction) -> Unit,
    snackBarHostState: SnackbarHostState
) {
    val scrollBehaviour = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    Scaffold(
        modifier = Modifier
            .padding(horizontal = 50.dp)
            .nestedScroll(scrollBehaviour.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(
                scrollBehavior = scrollBehaviour,
                title = { Text(text = stringResource(Res.string.dashboard)) }
            )
        },
        content = { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(IntrinsicSize.Min),
                        horizontalArrangement = Arrangement.spacedBy(15.dp)
                    ) {
                        ServerCard(
                            modifier = Modifier
                                .weight(0.65f)
                                .fillMaxHeight(),
                            state = state,
                            onPortChange = { onAction(DashboardAction.Port(it)) },
                            onHostChange = { onAction(DashboardAction.Host(it)) },
                            onSaveClick = { onAction(DashboardAction.SaveConfiguration) },
                            onToggleClick = { onAction(DashboardAction.ToggleServer) }
                        )
                        TunnelCard(
                            modifier = Modifier
                                .weight(0.35f)
                                .fillMaxHeight(),
                            state = state,
                            onToggleClick = { onAction(DashboardAction.ToggleTunnel) }
                        )
                    }
                }
            }
        },
        snackbarHost = {
            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.CenterEnd
            ) {
                SnackbarHost(hostState = snackBarHostState)
            }
        }
    )
}

@Composable
@Preview(showBackground = true)
private fun Preview() {
    DashboardExpanded(
        state = DashboardState(),
        onAction = {},
        snackBarHostState = SnackbarHostState()
    )
}