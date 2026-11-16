package com.a.server.presentation.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable

@Composable
fun <T: Any> FadeAnimatedContent(
    targetState: T,
    content: @Composable (AnimatedContentScope.(T) -> Unit)
) {
    AnimatedContent(
        targetState = targetState,
        transitionSpec = {
            fadeIn() togetherWith fadeOut()
        },
        content = { content(it) }
    )
}