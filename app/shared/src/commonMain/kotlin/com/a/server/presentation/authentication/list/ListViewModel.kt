package com.a.server.presentation.authentication.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.a.server.domain.repository.AuthenticationRepository
import com.a.server.navigation.NavigationEffect
import com.a.server.presentation.ScreenEffect
import com.a.server.presentation.mapper.AuthenticationMapper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
class ListViewModel(
    private val authenticationRepository: AuthenticationRepository,
    private val screenEffect: ScreenEffect,
): ViewModel() {
    private val _state = MutableStateFlow(ListState())
    val state = _state.asStateFlow()

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

    fun onAction(action: ListAction) {
        when (action) {
            is ListAction.Search -> {
                search(query = action.newSearch)
            }
            is ListAction.Select -> {
                select(id = action.newId)
            }
            ListAction.DeleteAlertDialog -> {
                _state.update { currentState ->
                    currentState.copy(isDeleteDialogVisible = !currentState.isDeleteDialogVisible)
                }
            }
            ListAction.Delete -> {
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
                searchKey = query,
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
                    screenEffect.sendEffect(NavigationEffect.ShowSnackBar(message))
                    _state.update { currentState ->
                        currentState.copy(
                            selectedAuthenticationId = emptySet()
                        )
                    }
                }.onLeft { message ->
                    screenEffect.sendEffect(NavigationEffect.ShowSnackBar(message))
                }
            }
        }
    }
}