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
            tableRepository.getList().collect { either ->
                either.onRight { tableModels ->
                    _state.update { currentState ->
                        currentState.copy(
                            isTableListLoading = false,
                            tableList = tableModels
                        )
                    }
                    searchKey(key = "")
                }.onLeft { message ->
                    _state.update { currentState ->
                        currentState.copy(
                            isTableListLoading = false,
                            isTableListError = message
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
        val filteredTableList = if (key.isBlank()) {
            _state.value.tableList
        } else {
            _state.value.tableList.filter { tableModel ->
                tableModel.tableName.contains(other = key, ignoreCase = true)
            }
        }
        _state.update { currentState ->
            currentState.copy(
                searchKey = key,
                filteredTableList = filteredTableList
            )
        }
    }

    private fun deleteTable(tableName: String) {
        viewModelScope.launch {
            tableRepository.delete(
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