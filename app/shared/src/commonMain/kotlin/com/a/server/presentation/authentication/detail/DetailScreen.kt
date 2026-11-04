package com.a.server.presentation.authentication.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedListItem
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
import aserver.app.shared.generated.resources.detail
import aserver.app.shared.generated.resources.warning
import com.a.server.navigation.authentication.AuthenticationNavigator
import com.a.server.presentation.model.authentication.AuthenticationUi
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
    authId: String,
    navigator: AuthenticationNavigator = koinInject(),
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
        navigator = navigator,
        state = state,
        onAction = viewModel::onAction,
        snackBarHostState = snackBarHostState
    )
}

@Composable
private fun Content(
    navigator: AuthenticationNavigator,
    state: DetailState,
    onAction: (DetailAction) -> Unit,
    snackBarHostState: SnackbarHostState
) {
    Scaffold(
        topBar = {
            TransparentTopAppBar(
                onNavigationClick = { navigator.navigateBack() },
                title = stringResource(Res.string.detail)
            )
        },
        content = { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .padding(innerPadding),
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
                itemsIndexed(
                    items = state.authenticationList,
                    key = { index, simpleItem -> index }
                ) { index, simpleItem ->
                    SegmentedListItem(
                        modifier = Modifier
                            .animateItem(),
                        colors = ListItemDefaults.segmentedColors(
                            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                        ),
                        shapes = ListItemDefaults.segmentedShapes(index, state.authenticationList.size),
                        leadingContent = { Icon(simpleItem.icon.asIcon(), null) },
                        content = { Text(text = simpleItem.title.asString()) },
                        supportingContent = { Text(text = simpleItem.content) }
                    )
                }
            }
        },
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) }
    )
}

@Composable
@Preview
private fun Preview() {
    Content(
        navigator = AuthenticationNavigator(),
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