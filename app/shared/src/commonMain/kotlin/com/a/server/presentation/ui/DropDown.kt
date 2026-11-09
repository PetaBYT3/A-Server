package com.a.server.presentation.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExposedDropdownMenu
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuBoxScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.util.fastForEach

@Composable
fun <T: Any> GenericStandardDropDownMenu(
    modifier: Modifier = Modifier,
    content: @Composable (show: () -> Unit) -> Unit,
    items: List<T>,
    itemContent: @Composable (item: T, dismiss: () -> Unit) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    val show: () -> Unit = { expanded = true }
    val dismiss: () -> Unit = { expanded = false }

    Box(
        modifier = modifier
    ) {
        content(show)
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            items.fastForEach { item ->
                itemContent(item, dismiss)
            }
        }
    }
}

@Composable
fun <T: Any> GenericExposedDropDownMenu(
    modifier: Modifier = Modifier,
    content: @Composable (ExposedDropdownMenuBoxScope.(show: () -> Unit) -> Unit),
    items: List<T>,
    itemContent: @Composable (ExposedDropdownMenuBoxScope.(item: T, dismiss: () -> Unit) -> Unit),
) {
    var expanded by remember { mutableStateOf(false) }
    val show: () -> Unit = { expanded = true }
    val dismiss: () -> Unit = { expanded = false }

    Box(
        modifier = modifier
    ) {
        ExposedDropdownMenuBox(
            modifier = Modifier,
            expanded = expanded,
            onExpandedChange = { expanded = it }
        ) {
            content(this, show)
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                content = {
                    items.fastForEach { item ->
                        itemContent(item, dismiss)
                    }
                }
            )
        }
    }
}