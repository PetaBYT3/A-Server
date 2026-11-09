package com.a.server.presentation.database.detail.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun Cell(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    isField: Boolean = false,
    value: String
) {
    Card(
        modifier = modifier,
        onClick = { if (onClick != null) onClick() },
        colors = CardDefaults.cardColors(
            containerColor = if (isField) {
                MaterialTheme.colorScheme.secondaryContainer
            } else {
                MaterialTheme.colorScheme.surfaceContainerHigh
            },
            contentColor = if (isField) {
                MaterialTheme.colorScheme.onSecondaryContainer
            } else {
                MaterialTheme.colorScheme.onSurfaceVariant
            }
        ),
        shape = MaterialTheme.shapes.extraSmall
    ) {
        Box(
            modifier = Modifier
                .padding(start = 5.dp, end = 5.dp, top = 15.dp, bottom = 15.dp)
                .width(100.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = value,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
@Preview
private fun Preview() {
    Cell(
        isField = true,
        value = "Valueeeeeeeeeeeee",
    )
}