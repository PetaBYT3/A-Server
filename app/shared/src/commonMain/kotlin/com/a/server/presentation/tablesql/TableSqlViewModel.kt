package com.a.server.presentation.tablesql

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
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
class TableSqlViewModel(
    private val tableRepository: TableRepository
): ViewModel() {
    private val _state = MutableStateFlow(TableSqlState())
    val state = _state.asStateFlow()

    private val _effect = Channel<Effect>(Channel.UNLIMITED)
    val effect = _effect.receiveAsFlow()

    fun onAction(action: TableSqlAction) {
        when (action) {
            is TableSqlAction.Query -> {
                _state.update { currentState ->
                    currentState.copy(query = action.newQuery)
                }
            }
            TableSqlAction.Execute -> {
                execute()
            }
        }
    }

    private fun execute() {
        viewModelScope.launch {
            tableRepository.executeSql(
                query = _state.value.query
            ).collect { either ->
                either.onRight { message ->
                    _effect.send(Effect.ShowSnackBar(message))
                }.onLeft { message ->
                    _effect.send(Effect.ShowSnackBar(message))
                }
            }
        }
    }
}