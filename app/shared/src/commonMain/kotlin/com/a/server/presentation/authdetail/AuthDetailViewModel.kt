package com.a.server.presentation.authdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.a.server.domain.repository.AuthenticationRepository
import com.a.server.presentation.mapper.AuthenticationMapper
import com.a.server.presentation.util.Effect
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.annotation.InjectedParam
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
class AuthDetailViewModel(
    @InjectedParam
    private val authId: String,
    private val authenticationRepository: AuthenticationRepository
): ViewModel() {
    private val _state = MutableStateFlow(AuthDetailState())
    val state = _state.asStateFlow()

    private val _effect = Channel<Effect>()
    val effect = _effect.receiveAsFlow()

    init {
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

    fun onAction(action: AuthDetailAction) {

    }
}