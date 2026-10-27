package com.a.server.presentation.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.a.server.domain.repository.ServerRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
class DashboardViewModel(
    private val serverRepository: ServerRepository
): ViewModel() {
    private val _state = MutableStateFlow(DashboardState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            serverRepository.isRunning.collect { bool ->
                _state.update { currentState ->
                    currentState.copy(isServerRunning = bool)
                }
            }
        }

        viewModelScope.launch {
            serverRepository.logs.collect { logs ->
                _state.update { currentState ->
                    currentState.copy(logs = logs)
                }
            }
        }
    }

    fun onAction(action: DashboardAction) {
        when (action) {
            DashboardAction.ToggleServer -> {
                toggleServer()
            }
        }
    }

    private fun toggleServer() {
        if (serverRepository.isRunning.value) {
            serverRepository.stop()
        } else {
            serverRepository.start()
        }
    }
}