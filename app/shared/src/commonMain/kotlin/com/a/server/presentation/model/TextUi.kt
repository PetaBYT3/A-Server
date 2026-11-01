package com.a.server.presentation.model

import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

sealed interface TextUi {
    data class Dynamic(val value: String) : TextUi
    data class Resource(val resId: StringResource) : TextUi

    @Composable
    fun asString(): String {
        return when (this) {
            is Dynamic -> value
            is Resource -> stringResource(resId)
        }
    }
}