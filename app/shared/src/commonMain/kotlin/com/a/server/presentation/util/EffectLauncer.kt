package com.a.server.presentation.util

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import com.a.server.navigation.Navigator
import kotlinx.coroutines.flow.Flow
import org.koin.compose.koinInject

@Composable
fun EffectLauncher(
    snackBarHostState: SnackbarHostState,
    screenEffect: Flow<Effect>
) {
    val navigator: Navigator = koinInject()
    val lifeCycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(lifeCycleOwner.lifecycle, screenEffect) {
        lifeCycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            screenEffect.collect { effect ->
                when (effect) {
                    is Effect.ShowSnackBar -> {
                        snackBarHostState.showSnackbar(
                            message = effect.message,
                            withDismissAction = true
                        )
                    }
                    Effect.NavigateBack -> {
                        navigator.navigateBack()
                    }
                }
            }
        }
    }
}