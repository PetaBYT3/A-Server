package com.a.server.presentation.model

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.vectorResource

sealed interface IconUi {
    data class Vector(val imageVector: ImageVector) : IconUi
    data class Resource(val resId: DrawableResource) : IconUi

    @Composable
    fun asIcon(): ImageVector {
        return when (this) {
            is Vector -> imageVector
            is Resource -> vectorResource(resId)
        }
    }
}