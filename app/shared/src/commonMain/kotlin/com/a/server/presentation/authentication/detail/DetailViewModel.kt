package com.a.server.presentation.authentication.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.a.server.domain.repository.AuthenticationRepository
import com.a.server.navigation.NavigationEffect
import com.a.server.presentation.ScreenEffect
import com.a.server.presentation.mapper.AuthenticationMapper
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.annotation.InjectedParam
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
class DetailViewModel(
    @InjectedParam
    private val authId: String,
    private val authenticationRepository: AuthenticationRepository,
    private val screenEffect: ScreenEffect
): ViewModel() {
    private val _state = MutableStateFlow(DetailState())
    val state = _state.asStateFlow()

    private val _Navigation_effect = Channel<NavigationEffect>()
    val effect = _Navigation_effect.receiveAsFlow()

    init {
        getAuthentication()
    }

    fun getAuthentication() {
        viewModelScope.launch {
            authenticationRepository.getSingle(
                authenticationId = authId
            ).collect { either ->
                either.onRight { authenticationModel ->
                    _state.update { currentState ->
                        currentState.copy(
                            isAuthLoading = false,
                            authentication = AuthenticationMapper.toUi(authenticationModel)
                        )
                    }
                }.onLeft { message ->
                    _state.update { currentState ->
                        currentState.copy(
                            isAuthLoading = false,
                            isAuthError = message
                        )
                    }
                }
            }
        }
    }

    fun onAction(action: DetailAction) {
        when (action) {
            DetailAction.DialogAlertDeleteAuthentication -> {
                _state.update { currentState ->
                    currentState.copy(isDialogAlertDeleteAuthenticationVisible = !currentState.isDialogAlertDeleteAuthenticationVisible)
                }
            }
            DetailAction.DeleteAuthentication -> {
                deleteAuthentication()
            }
        }
    }

    private fun deleteAuthentication() {
        viewModelScope.launch {
            authenticationRepository.delete(
                ids = setOf(authId)
            ).collect { either ->
                either.onRight { message ->
                    screenEffect.sendEffect(NavigationEffect.NavigateBack)
                    screenEffect.sendEffect(NavigationEffect.ShowSnackBar(message))
                }.onLeft { message ->
                    screenEffect.sendEffect(NavigationEffect.ShowSnackBar(message))
                }
            }
        }
    }
}