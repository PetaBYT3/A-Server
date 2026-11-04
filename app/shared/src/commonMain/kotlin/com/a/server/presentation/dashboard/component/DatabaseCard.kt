package com.a.server.presentation.dashboard.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import aserver.app.shared.generated.resources.Res
import aserver.app.shared.generated.resources.database_24px
import aserver.app.shared.generated.resources.sqlite_database
import com.a.server.presentation.dashboard.main.MainState
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

@Composable
fun DatabaseCard(
    modifier: Modifier = Modifier,
    state: MainState
) {
    Card(
        modifier = modifier,
        shape = AbsoluteRoundedCornerShape(16.dp)
    ) {
        Column {
            ListItem(
                colors = ListItemDefaults.colors(
                    containerColor = Color.Transparent
                ),
                leadingContent = {
                    Icon(
                        imageVector = vectorResource(Res.drawable.database_24px),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
                content = {
                    Text(
                        text = stringResource(Res.string.sqlite_database),
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                supportingContent = {
                    Text(
                        text = state.storageState.usedAndFreeSize,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            )
        }
    }
}

@Composable
@Preview
private fun Preview() {
    DatabaseCard(
        state = MainState()
    )
}