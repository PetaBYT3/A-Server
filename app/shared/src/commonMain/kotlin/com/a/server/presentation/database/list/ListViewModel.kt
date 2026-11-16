package com.a.server.presentation.database.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.a.server.domain.repository.DatabaseRepository
import com.a.server.presentation.util.Effect
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
class ListViewModel(
    private val databaseRepository: DatabaseRepository
): ViewModel() {
    private val _state = MutableStateFlow(ListState())
    val state = _state.asStateFlow()

    private val _effect = Channel<Effect>(Channel.UNLIMITED)
    val effect = _effect.receiveAsFlow()

    init {
        viewModelScope.launch {
            databaseRepository.getTables().collect { either ->
                either.onRight { tableModels ->
                    _state.update { currentState ->
                        currentState.copy(
                            isTablesLoading = false,
                            tables = tableModels
                        )
                    }
                    searchKey(key = "")
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

    fun onAction(action: ListAction) {
        when (action) {
            is ListAction.SearchKey -> {
                searchKey(key = action.newKey)
            }
            is ListAction.DeleteTable -> {
                deleteTable(tableName = action.tableName)
            }
        }
    }

    private fun searchKey(key: String) {
        val filteredTables = if (key.isBlank()) {
            _state.value.tables
        } else {
            _state.value.tables.filter { tableModel ->
                tableModel.table.name.contains(other = key, ignoreCase = true)
            }
        }
        _state.update { currentState ->
            currentState.copy(
                searchKey = key,
                filteredTables = filteredTables
            )
        }
    }

    private fun deleteTable(tableName: String) {
        viewModelScope.launch {
            databaseRepository.deleteTable(
                tableNames = listOf(tableName)
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