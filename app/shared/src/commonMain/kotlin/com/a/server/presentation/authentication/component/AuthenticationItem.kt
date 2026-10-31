package com.a.server.presentation.authentication.component

import androidx.compose.material3.Checkbox
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.a.server.presentation.model.authentication.AuthenticationUi

@Composable
fun AuthenticationItem(
    modifier: Modifier = Modifier,
    index: Int = 0,
    count: Int = 1,
    onClick: (String) -> Unit,
    selected: Boolean,
    authenticationUi: AuthenticationUi
) {
    SegmentedListItem(
        modifier = modifier,
        shapes = ListItemDefaults.segmentedShapes(index = index, count = count),
        colors = ListItemDefaults.segmentedColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHighest
        ),
        onClick = { onClick(authenticationUi.id) },
        overlineContent = {
            Text(
                text = authenticationUi.id,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        leadingContent = {
            Checkbox(
                checked = selected,
                onCheckedChange = null
            )
        },
        content = {
            Text(
                text = authenticationUi.email,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        trailingContent = {
            Text(
                text = authenticationUi.createdAt,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    )
}

@Composable
@Preview
private fun Preview() {
    AuthenticationItem(
        selected = false,
        onClick = {},
        authenticationUi = AuthenticationUi(
            id = "d23a8d0wa9d",
            email = "preview@mail.com",
            password = "password123",
            createdAt = "Nov 1 2023"
        )
    )
}