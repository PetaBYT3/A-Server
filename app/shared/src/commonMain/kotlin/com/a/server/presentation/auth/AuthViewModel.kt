package com.a.server.presentation.auth

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
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
class AuthViewModel(
    private val authenticationRepository: AuthenticationRepository
): ViewModel() {
    private val _state = MutableStateFlow(AuthState())
    val state = _state.asStateFlow()

    private val _effect = Channel<Effect>(Channel.UNLIMITED)
    val effect = _effect.receiveAsFlow()

    init {
        viewModelScope.launch {
            authenticationRepository.getList().collect { either ->
                either.onRight { authenticationModels ->
                    _state.update { currentState ->
                        currentState.copy(
                            authenticationList = authenticationModels.map { AuthenticationMapper.toUi(it) },
                            isAuthenticationListLoading = false
                        )
                    }
                    search(query = "")
                }.onLeft { message ->
                    _state.update { currentState ->
                        currentState.copy(
                            isAuthenticationListError = message,
                            isAuthenticationListLoading = false
                        )
                    }
                }
            }
        }
    }

    fun onAction(action: AuthAction) {
        when (action) {
            is AuthAction.Search -> {
                search(query = action.newSearch)
            }
            is AuthAction.Select -> {
                select(id = action.newId)
            }
            AuthAction.DeleteAlertDialog -> {
                _state.update { currentState ->
                    currentState.copy(isDeleteDialogVisible = !currentState.isDeleteDialogVisible)
                }
            }
            AuthAction.Delete -> {
                delete()
            }
        }
    }

    private fun search(query: String) {
        val filteredAuthenticationList = if (query.isBlank()) {
            _state.value.authenticationList
        } else {
            _state.value.authenticationList.filter { authenticationUi ->
                authenticationUi.email.contains(other = query, ignoreCase = true)
            }
        }

        _state.update { currentState ->
            currentState.copy(
                search = query,
                filteredAuthenticationList = filteredAuthenticationList
            )
        }
    }

    private fun select(id: String) {
        val isAlreadySelected = id in _state.value.selectedAuthenticationId

        _state.update { currentState ->
            currentState.copy(
                selectedAuthenticationId = if (isAlreadySelected) {
                    currentState.selectedAuthenticationId - id
                } else {
                    currentState.selectedAuthenticationId + id
                }
            )
        }
    }

    private fun delete() {
        viewModelScope.launch {
            authenticationRepository.delete(
                ids = _state.value.selectedAuthenticationId
            ).collect { either ->
                either.onRight { message ->
                    _effect.send(Effect.ShowSnackBar(message))
                    _state.update { currentState ->
                        currentState.copy(
                            selectedAuthenticationId = emptySet()
                        )
                    }
                }.onLeft { message ->
                    _effect.send(Effect.ShowSnackBar(message))
                }
            }
        }
    }
}