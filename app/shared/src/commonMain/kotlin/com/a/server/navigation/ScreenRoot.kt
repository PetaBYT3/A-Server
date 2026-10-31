@file:OptIn(ExperimentalSerializationApi::class, ExperimentalMaterial3AdaptiveApi::class)

package com.a.server.navigation

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2
import androidx.compose.material3.adaptive.layout.calculatePaneScaffoldDirective
import androidx.compose.material3.adaptive.navigation3.rememberListDetailSceneStrategy
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.a.server.presentation.authentication.AuthenticationScreen
import com.a.server.presentation.dashboard.DashboardScreen
import kotlinx.serialization.ExperimentalSerializationApi
import org.koin.compose.koinInject

@Composable
fun ScreenRoot() {
    val navigator: Navigator = koinInject()

    val windowAdaptiveInfo = currentWindowAdaptiveInfoV2()
    val directive = remember(windowAdaptiveInfo) {
        calculatePaneScaffoldDirective(windowAdaptiveInfo).copy(
            horizontalPartitionSpacerSize = 0.dp
        )
    }
    val listDetailStrategy = rememberListDetailSceneStrategy<Any>(
        directive = directive
    )

    val transitionDuration = 250
    NavDisplay(
        backStack = navigator.backStack,
        onBack = { navigator.navigateBack() },
        sceneStrategies = listOf(listDetailStrategy),
        entryProvider = entryProvider {
            entry<Route.DashboardScreen>(
                content = { DashboardScreen(navigator) }
            )
            entry<Route.AuthenticationScreen>(
                content = { AuthenticationScreen(navigator) }
            )
        },
        transitionSpec = {
            slideInHorizontally(
                initialOffsetX = { it },
                animationSpec = tween(transitionDuration, easing = FastOutSlowInEasing)
            ) togetherWith slideOutHorizontally(
                targetOffsetX = { -it / 3 },
                animationSpec = tween(transitionDuration, easing = FastOutSlowInEasing)
            )
        },
        popTransitionSpec = {
            slideInHorizontally(
                initialOffsetX = { -it / 3 },
                animationSpec = tween(transitionDuration, easing = FastOutSlowInEasing)
            ) togetherWith slideOutHorizontally(
                targetOffsetX = { it },
                animationSpec = tween(transitionDuration, easing = FastOutSlowInEasing)
            )
        },
        predictivePopTransitionSpec = {
            slideInHorizontally(
                initialOffsetX = { -it / 3 },
                animationSpec = tween(transitionDuration, easing = FastOutSlowInEasing)
            ) togetherWith slideOutHorizontally(
                targetOffsetX = { it },
                animationSpec = tween(transitionDuration, easing = FastOutSlowInEasing)
            )
        }
    )
}