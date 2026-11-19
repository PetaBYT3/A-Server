package com.a.server.presentation.database.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.a.server.domain.model.sqlite.RowModel
import com.a.server.domain.repository.DatabaseRepository
import com.a.server.navigation.NavigationEffect
import com.a.server.presentation.ScreenEffect
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.annotation.InjectedParam
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
class DetailViewModel(
    @InjectedParam
    private val tableName: String,
    private val databaseRepository: DatabaseRepository,
    private val screenEffect: ScreenEffect
): ViewModel() {
    private val _state = MutableStateFlow(DetailState())
    val state = _state.asStateFlow()

    init {
        getTable()
    }

    private fun getTable() {
        viewModelScope.launch {
            databaseRepository.getTable(
                name = tableName
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

    fun onAction(action: DetailAction) {
        when (action) {
            DetailAction.DeleteTableDialog -> {
                _state.update { currentState ->
                    currentState.copy(isDeleteTableDialogVisible = !currentState.isDeleteTableDialogVisible)
                }
            }
            DetailAction.DeleteTable -> {
                deleteTable()
            }
            is DetailAction.SelectRow -> {
                selectRow(rowModel = action.rowModel)
            }
            DetailAction.SelectAllRow -> {
                _state.update { currentState ->
                    currentState.copy(selectedRows = currentState.tableComplete?.rows ?: emptyList())
                }
            }
            DetailAction.UnselectAllRow -> {
                _state.update { currentState ->
                    currentState.copy(selectedRows = emptyList())
                }
            }
            DetailAction.DeleteRowDialog -> {
                _state.update { currentState ->
                    currentState.copy(isDeleteRowsDialogVisible = !currentState.isDeleteRowsDialogVisible)
                }
            }
            DetailAction.DeleteRow -> {
                deleteRow()
            }
        }
    }

    private fun deleteTable() {
        viewModelScope.launch {
            databaseRepository.deleteTable(
                tableNames = listOf(tableName)
            ).collect { either ->
                either.onRight {
                    screenEffect.sendEffect(NavigationEffect.NavigateBack)
                }.onLeft { message ->
                    screenEffect.sendEffect(NavigationEffect.ShowSnackBar(message))
                }
            }
        }
    }

    private fun selectRow(rowModel: RowModel) {
        val isAlreadySelected = rowModel in _state.value.selectedRows

        _state.update { currentState ->
            currentState.copy(
                selectedRows = if (isAlreadySelected) {
                    currentState.selectedRows - rowModel
                } else {
                    currentState.selectedRows + rowModel
                }
            )
        }
    }

    private fun deleteRow() {
        viewModelScope.launch {
            databaseRepository.deleteRow(
                tableName = _state.value.tableComplete?.tableWithFields?.table?.name ?: return@launch,
                rows = _state.value.selectedRows.toList()
            ).collect { either ->
                either.onRight { message ->
                    screenEffect.sendEffect(NavigationEffect.ShowSnackBar(message))
                    _state.update { currentState ->
                        currentState.copy(selectedRows = emptyList())
                    }
                }.onLeft { message ->
                    screenEffect.sendEffect(NavigationEffect.ShowSnackBar(message))
                }
            }
        }
    }
}