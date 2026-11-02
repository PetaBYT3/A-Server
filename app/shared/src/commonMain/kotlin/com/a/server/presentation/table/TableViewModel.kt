package com.a.server.presentation.table

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
class TableViewModel(
    private val tableRepository: TableRepository
): ViewModel() {
    private val _state = MutableStateFlow(TableState())
    val state = _state.asStateFlow()

    private val _effect = Channel<Effect>(Channel.UNLIMITED)
    val effect = _effect.receiveAsFlow()

    init {
        viewModelScope.launch {
            tableRepository.getTables().collect { either ->
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

    fun onAction(action: TableAction) {
        when (action) {
            is TableAction.SearchKey -> {
                searchKey(key = action.newKey)
            }
            is TableAction.DeleteTable -> {
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
            tableRepository.deleteTable(
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