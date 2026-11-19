package com.a.server.presentation

import com.a.server.navigation.NavigationEffect
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import org.koin.core.annotation.Single

@Single
class ScreenEffect {
    private val _Navigation_effect = Channel<NavigationEffect>(Channel.UNLIMITED)
    val effect = _Navigation_effect.receiveAsFlow()

    suspend fun sendEffect(navigationEffect: NavigationEffect) {
        _Navigation_effect.send(navigationEffect)
    }
}