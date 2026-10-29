package com.a.server.presentation.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.a.server.domain.repository.ServerRepository
import com.a.server.domain.repository.SettingsRepository
import com.a.server.domain.repository.TunnelRepository
import com.a.server.presentation.mapper.ServerMapper
import com.a.server.presentation.mapper.TunnelMapper
import com.a.server.presentation.util.Effect
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
class DashboardViewModel(
    private val settingsRepository: SettingsRepository,
    private val serverRepository: ServerRepository,
    private val tunnelRepository: TunnelRepository
): ViewModel() {
    private val _state = MutableStateFlow(DashboardState())
    val state = _state.asStateFlow()

    private val _effect = Channel<Effect>(Channel.UNLIMITED)
    val effect = _effect.receiveAsFlow()

    init {
        viewModelScope.launch {
            settingsRepository.serverPort.collect { port ->
                _state.update { currentState ->
                    currentState.copy(serverPort = port.toString())
                }
            }
        }

        viewModelScope.launch {
            settingsRepository.serverHost.collect { host ->
                _state.update { currentState ->
                    currentState.copy(serverHost = host)
                }
            }
        }

        viewModelScope.launch {
            serverRepository.serverState.collect { serverModel ->
                _state.update { currentState ->
                    currentState.copy(serverState = ServerMapper.toUi(serverModel))
                }
            }
        }

        viewModelScope.launch {
            tunnelRepository.tunnelState.collect { tunnelModel ->
                _state.update { currentState ->
                    currentState.copy(tunnelState = TunnelMapper.toUi(tunnelModel))
                }
            }
        }
    }

    fun onAction(action: DashboardAction) {
        when (action) {
            is DashboardAction.Port -> {
                _state.update { currentState ->
                    currentState.copy(
                        serverPort = action.newPort
                    )
                }
            }
            is DashboardAction.Host -> {
                _state.update { currentState ->
                    currentState.copy(
                        serverHost = action.newHost
                    )
                }
            }
            DashboardAction.SaveConfiguration -> {
                saveConfiguration()
            }
            DashboardAction.ToggleServer -> {
                toggleServer()
            }
            DashboardAction.ToggleTunnel -> {
                toggleTunnel()
            }
        }
    }

    private fun saveConfiguration() {
        viewModelScope.launch {
            settingsRepository.setServerPortAndHost(
                port = _state.value.serverPort.toInt(),
                host = _state.value.serverHost
            ).collect { either ->
                either.onRight { message ->
                    _effect.send(Effect.ShowSnackBar(message))
                }.onLeft { message ->
                    _effect.send(Effect.ShowSnackBar(message))
                }
            }
        }
    }

    private fun toggleServer() {
        viewModelScope.launch {
            serverRepository.toggle().onStart {
                _state.update { currentState ->
                    currentState.copy(isServerLoading = true)
                }
            }.onCompletion {
                _state.update { currentState ->
                    currentState.copy(isServerLoading = false)
                }
            }.collect { either ->
                either.onRight { message ->
                    _effect.send(Effect.ShowSnackBar(message))
                }.onLeft { message ->
                    _effect.send(Effect.ShowSnackBar(message))
                }
            }
        }
    }

    private fun toggleTunnel() {
        viewModelScope.launch {
            tunnelRepository.toggle().onStart {
                _state.update { currentState ->
                    currentState.copy(isTunnelLoading = true)
                }
            }.onCompletion {
                _state.update { currentState ->
                    currentState.copy(isTunnelLoading = false)
                }
            }.collect { either ->
                either.onRight { message ->
                    _effect.send(Effect.ShowSnackBar(message))
                }.onLeft { message ->
                    _effect.send(Effect.ShowSnackBar(message))
                }
            }
        }
    }
}