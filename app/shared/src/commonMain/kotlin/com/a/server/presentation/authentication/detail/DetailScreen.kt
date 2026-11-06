package com.a.server.presentation.authentication.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CalendarToday
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Password
import androidx.compose.material.icons.rounded.Tag
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import aserver.app.shared.generated.resources.Res
import aserver.app.shared.generated.resources.created_at
import aserver.app.shared.generated.resources.email
import aserver.app.shared.generated.resources.id
import aserver.app.shared.generated.resources.password
import aserver.app.shared.generated.resources.warning
import com.a.server.navigation.NavigationState
import com.a.server.presentation.authentication.detail.component.DetailRow
import com.a.server.presentation.model.authentication.AuthenticationUi
import com.a.server.presentation.ui.CenterContainedLoadingIndicator
import com.a.server.presentation.ui.MessageCard
import com.a.server.presentation.util.EffectLauncher
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun AuthenticationDetailScreen(
    authId: String,
    navigationState: NavigationState = koinInject(),
    viewModel: DetailViewModel = koinViewModel(
        parameters = {
            parametersOf(authId)
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
    state: DetailState,
    onAction: (DetailAction) -> Unit,
    snackBarHostState: SnackbarHostState
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        contentPadding = PaddingValues(start = 15.dp, end = 15.dp, bottom = 100.dp),
        verticalArrangement = Arrangement.spacedBy(2.5.dp)
    ) {
        if (state.isAuthLoading) {
            item("isAuthLoading") {
                CenterContainedLoadingIndicator(
                    modifier = Modifier
                        .animateItem()
                )
            }
            return@LazyColumn
        }
        if (state.isAuthError != null) {
            item("isAuthError") {
                MessageCard(
                    modifier = Modifier
                        .animateItem(),
                    icon = Icons.Rounded.Warning,
                    title = stringResource(Res.string.warning),
                    message = state.isAuthError
                )
            }
            return@LazyColumn
        }
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                )
            ) {
                Column {
                    DetailRow(
                        icon = Icons.Rounded.Tag,
                        title = stringResource(Res.string.id),
                        text = state.authentication.id
                    )
                    DetailRow(
                        icon = Icons.Rounded.Email,
                        title = stringResource(Res.string.email),
                        text = state.authentication.email
                    )
                    DetailRow(
                        icon = Icons.Rounded.Password,
                        title = stringResource(Res.string.password),
                        text = state.authentication.password
                    )
                    DetailRow(
                        icon = Icons.Rounded.CalendarToday,
                        title = stringResource(Res.string.created_at),
                        text = state.authentication.createdAt
                    )
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
        state = DetailState(
            authentication = AuthenticationUi(
                id = "9AWA8D9AW8AD9S8",
                email = "preview@mail.com"
            )
        ),
        onAction = {  },
        snackBarHostState = SnackbarHostState()
    )
}