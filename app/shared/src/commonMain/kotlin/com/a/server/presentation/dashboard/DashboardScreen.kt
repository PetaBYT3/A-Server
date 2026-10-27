package com.a.server.presentation.dashboard

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.a.server.navigation.Navigator
import com.a.server.presentation.dashboard.layout.DashboardExpanded
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun DashboardScreen(
    navigator: Navigator,
    viewModel: DashboardViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Dashboard") }
            )
        },
        content = { innerPadding ->
            DashboardExpanded(
                modifier = Modifier
                    .padding(innerPadding),
                state = state,
                onAction = viewModel::onAction
            )
        }
    )
}