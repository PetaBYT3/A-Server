package com.a.server.presentation.authentication.layout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import aserver.app.shared.generated.resources.Res
import aserver.app.shared.generated.resources.empty
import aserver.app.shared.generated.resources.error
import aserver.app.shared.generated.resources.users_empty
import com.a.server.navigation.Navigator
import com.a.server.presentation.authentication.AuthenticationAction
import com.a.server.presentation.authentication.AuthenticationState
import com.a.server.presentation.authentication.component.AuthenticationItem
import com.a.server.presentation.ui.CenterContainedLoadingIndicator
import com.a.server.presentation.ui.MessageCard
import com.a.server.presentation.ui.SimpleTextFieldSearchBar
import org.jetbrains.compose.resources.stringResource

@Composable
fun AuthenticationCompact(
    modifier: Modifier = Modifier,
    navigator: Navigator,
    state: AuthenticationState,
    onAction: (AuthenticationAction) -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        SimpleTextFieldSearchBar(
            modifier = Modifier
                .padding(horizontal = 15.dp),
            placeholder = "Search here...",
            query = state.search,
            onQueryChange = { onAction(AuthenticationAction.Search(it)) }
        )
        LazyColumn(
            modifier = Modifier
                .weight(1f),
            contentPadding = PaddingValues(start = 15.dp, end = 15.dp, bottom = 100.dp),
            verticalArrangement = Arrangement.spacedBy(2.5.dp)
        ) {
            if (state.isAuthenticationListLoading) {
                item("isAuthenticationListLoading") {
                    CenterContainedLoadingIndicator(
                        modifier = Modifier
                            .animateItem()
                    )
                }
                return@LazyColumn
            }
            if (state.isAuthenticationListError != null) {
                item("isAuthenticationListError") {
                    MessageCard(
                        modifier = Modifier
                            .animateItem(),
                        icon = Icons.Rounded.Warning,
                        title = stringResource(Res.string.error),
                        message = state.isAuthenticationListError
                    )
                }
                return@LazyColumn
            }
            if (state.filteredAuthenticationList.isEmpty()) {
                item("authenticationListIsEmpty") {
                    MessageCard(
                        modifier = Modifier
                            .animateItem(),
                        icon = Icons.Rounded.Warning,
                        title = stringResource(Res.string.empty),
                        message = stringResource(Res.string.users_empty)
                    )
                }
                return@LazyColumn
            }
            itemsIndexed(
                items = state.filteredAuthenticationList,
                key = { _, authenticationUi -> authenticationUi.id }
            ) { index, authenticationUi ->
                AuthenticationItem(
                    modifier = Modifier
                        .animateItem(),
                    index = index,
                    count = state.filteredAuthenticationList.size,
                    selected = authenticationUi.id in state.selectedAuthenticationId,
                    onClick = { onAction(AuthenticationAction.Select(authenticationUi.id)) },
                    authenticationUi = authenticationUi,
                )
            }
        }
    }
}