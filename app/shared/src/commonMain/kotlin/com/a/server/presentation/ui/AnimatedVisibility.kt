package com.a.server.presentation.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

enum class VisibilityDirection {
    Top, Bottom, Start, End
}

@Composable
fun DirectionalAnimatedVisibility(
    visible: Boolean,
    direction: VisibilityDirection,
    modifier: Modifier = Modifier,
    content: @Composable AnimatedVisibilityScope.() -> Unit
) {
    val enterTransition = remember(direction) {
        when (direction) {
            VisibilityDirection.Top -> expandVertically(expandFrom = Alignment.Top) + fadeIn()
            VisibilityDirection.Bottom -> expandVertically(expandFrom = Alignment.Bottom) + fadeIn()
            VisibilityDirection.Start -> expandHorizontally(expandFrom = Alignment.Start) + fadeIn()
            VisibilityDirection.End -> expandHorizontally(expandFrom = Alignment.End) + fadeIn()
        }
    }

    val exitTransition = remember(direction) {
        when (direction) {
            VisibilityDirection.Top -> shrinkVertically(shrinkTowards = Alignment.Top) + fadeOut()
            VisibilityDirection.Bottom -> shrinkVertically(shrinkTowards = Alignment.Bottom) + fadeOut()
            VisibilityDirection.Start -> shrinkHorizontally(shrinkTowards = Alignment.Start) + fadeOut()
            VisibilityDirection.End -> shrinkHorizontally(shrinkTowards = Alignment.End) + fadeOut()
        }
    }

    AnimatedVisibility(
        visible = visible,
        modifier = modifier,
        enter = enterTransition,
        exit = exitTransition,
        content = content
    )
}

@Composable
fun SlideUpAnimatedVisibility(
    modifier: Modifier = Modifier,
    visible: Boolean,
    content: @Composable (AnimatedVisibilityScope.() -> Unit)
) {
    AnimatedVisibility(
        modifier = modifier,
        enter = slideInVertically(
            initialOffsetY = { it },
            animationSpec = tween(durationMillis = 300)
        ) + fadeIn(animationSpec = tween(300)),
        exit = slideOutVertically(
            targetOffsetY = { it },
            animationSpec = tween(durationMillis = 300)
        ) + fadeOut(animationSpec = tween(300)),
        visible = visible,
        content = content
    )
}

@Composable
fun FadeAnimatedVisibility(
    visible: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable AnimatedVisibilityScope.() -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        modifier = modifier,
        enter = fadeIn(
            animationSpec = tween()
        ),
        exit = fadeOut(
            animationSpec = tween()
        ),
        content = content
    )
}