package com.a.server.presentation.authdetail

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CalendarToday
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Password
import androidx.compose.material.icons.rounded.Tag
import aserver.app.shared.generated.resources.Res
import aserver.app.shared.generated.resources.created_at
import aserver.app.shared.generated.resources.email
import aserver.app.shared.generated.resources.id
import aserver.app.shared.generated.resources.password
import com.a.server.presentation.model.IconUi
import com.a.server.presentation.model.SimpleItem
import com.a.server.presentation.model.TextUi
import com.a.server.presentation.model.authentication.AuthenticationUi

data class AuthDetailState(
    val isAuthLoading: Boolean = true,
    val isAuthError: String? = null,
    val authentication: AuthenticationUi = AuthenticationUi(),
) {
    val authenticationList: List<SimpleItem> = listOf(
        SimpleItem(
            icon = IconUi.Vector(Icons.Rounded.Tag),
            title = TextUi.Resource(Res.string.id),
            content = authentication.id
        ),
        SimpleItem(
            icon = IconUi.Vector(Icons.Rounded.Email),
            title = TextUi.Resource(Res.string.email),
            content = authentication.email
        ),
        SimpleItem(
            icon = IconUi.Vector(Icons.Rounded.Password),
            title = TextUi.Resource(Res.string.password),
            content = authentication.password
        ),
        SimpleItem(
            icon = IconUi.Vector(Icons.Rounded.CalendarToday),
            title = TextUi.Resource(Res.string.created_at),
            content = authentication.createdAt
        )
    )
}
