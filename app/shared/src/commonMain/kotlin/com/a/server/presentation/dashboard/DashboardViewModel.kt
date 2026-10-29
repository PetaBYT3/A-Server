package com.a.server.presentation.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.a.server.domain.repository.TunnelRepository
import com.a.server.presentation.util.Effect
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
class DashboardViewModel(
    private val tunnelRepository: TunnelRepository
): ViewModel() {
    private val _state = MutableStateFlow(DashboardState())
    val state = _state.asStateFlow()

    private val _effect = Channel<Effect>(Channel.CONFLATED)
    val effect = _effect.receiveAsFlow()

    init {
        viewModelScope.launch {
            tunnelRepository.state.collect { tunnelModel ->
                _state.update { currentState ->
                    currentState.copy(tunnelModel = tunnelModel)
                }
            }
        }
    }

    fun onAction(action: DashboardAction) {
        when (action) {
            DashboardAction.ToggleTunnel -> {
                toggleServer()
            }
        }
    }

    private fun toggleServer() {
        viewModelScope.launch {
            when (_state.value.tunnelModel.isRunning) {
                true -> tunnelRepository.stop()
                false -> tunnelRepository.start().collect { either ->
                    either.onLeft { message ->
                        _effect.send(Effect.ShowSnackBar(message))
                    }
                }
            }
        }
    }
}