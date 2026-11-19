package com.a.server.presentation.database.scheme

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.a.server.domain.repository.DatabaseRepository
import com.a.server.presentation.ScreenEffect
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
class SchemeViewModel(
    private val databaseRepository: DatabaseRepository,
    private val screenEffect: ScreenEffect
): ViewModel() {
    private val _state = MutableStateFlow(SchemeState())
    val state = _state.asStateFlow()

    init {
        getTables()
    }

    private fun getTables() {
        viewModelScope.launch {
            databaseRepository.getTables().collect { either ->
                either.onRight { tableWithFieldsModels ->
                    _state.update { currentState ->
                        currentState.copy(
                            isTablesLoading = false,
                            tables = tableWithFieldsModels
                        )
                    }
                }.onLeft { message ->
                    _state.update { currentState ->
                        currentState.copy(
                            isTablesLoading = false,
                            isTablesError = message
                        )
                    }
                }
            }
        }
    }

    fun onAction(action: SchemeAction) {

    }
}