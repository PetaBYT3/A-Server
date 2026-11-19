package com.a.server.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.Flow
import org.koin.compose.koinInject

@Composable
fun EffectLauncher(
    snackBarHostState: SnackbarHostState,
    screenNavigationEffect: Flow<NavigationEffect>,
    navigationState: NavigationState = koinInject()
) {
    val lifeCycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(lifeCycleOwner.lifecycle, screenNavigationEffect) {
        lifeCycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            screenNavigationEffect.collect { effect ->
                when (effect) {
                    is NavigationEffect.ShowSnackBar -> {
                        snackBarHostState.showSnackbar(
                            message = effect.message,
                            withDismissAction = true
                        )
                    }
                    NavigationEffect.NavigateBack -> {
                        navigationState.navigateBack()
                    }
                }
            }
        }
    }
}