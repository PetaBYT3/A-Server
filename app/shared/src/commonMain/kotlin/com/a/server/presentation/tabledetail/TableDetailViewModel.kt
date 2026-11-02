package com.a.server.presentation.tabledetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.a.server.domain.repository.TableRepository
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
class TableDetailViewModel(
    @InjectedParam
    private val name: String,
    private val tableRepository: TableRepository
): ViewModel() {
    private val _state = MutableStateFlow(TableDetailState())
    val state = _state.asStateFlow()

    private val _effect = Channel<Effect>(Channel.UNLIMITED)
    val effect = _effect.receiveAsFlow()

    init {
        viewModelScope.launch {
            tableRepository.getTable(
                name = name
            ).collect { either ->
                either.onRight { tableCompleteModel ->
                    _state.update { currentState ->
                        currentState.copy(
                            isTableCompleteLoading = false,
                            tableComplete = tableCompleteModel
                        )
                    }
                }.onLeft { message ->
                    _state.update { currentState ->
                        currentState.copy(
                            isTableCompleteLoading = false,
                            isTableCompleteError = message
                        )
                    }
                }
            }
        }
    }

    fun onAction(action: TableDetailAction) {

    }
}