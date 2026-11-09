package com.a.server.presentation

import com.a.server.presentation.util.Effect
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import org.koin.core.annotation.Single

@Single
class ScreenEffect {
    private val _effect = Channel<Effect>(Channel.UNLIMITED)
    val effect = _effect.receiveAsFlow()

    suspend fun sendEffect(effect: Effect) {
        _effect.send(effect)
    }
}