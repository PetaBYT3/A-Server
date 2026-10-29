package com.a.server.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun CustomCard(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(15.dp),
    title: @Composable (() -> Unit),
    content: @Composable () -> Unit
) {
    Card(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .padding(contentPadding),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            title()
            content()
        }
    }
}

@Composable
@Preview
private fun Preview() {
    CustomCard(
        title = { Text(text = "Preview Title") },
        content = { Text(text = "Preview Content") }
    )
}