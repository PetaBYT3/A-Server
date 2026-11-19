package com.a.server.presentation.database.upsertrow

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
class UpsertViewModel(
    @InjectedParam
    private val tableName: String,
    @InjectedParam
    private val rowId: String,
    private val databaseRepository: DatabaseRepository,
    private val screenEffect: ScreenEffect
): ViewModel() {
    private val _state = MutableStateFlow(UpsertState())
    val state = _state.asStateFlow()

    init {
        isOnEdit()
        getRow()
    }

    private fun isOnEdit() {
        _state.update { currentState ->
            currentState.copy(isOnEdit = tableName.isNotBlank())
        }
    }

    private fun getRow() {
        viewModelScope.launch {
            databaseRepository.getRow(
                tableName = tableName,
                rowId = rowId
            ).collect { either ->
                either.onRight { cellModels ->
                    _state.update { currentState ->
                        currentState.copy(
                            cells = cellModels,
                            isCellsLoading = false
                        )
                    }
                }.onLeft { message ->
                    _state.update { currentState ->
                        currentState.copy(
                            isCellsError = message,
                            isCellsLoading = false
                        )
                    }
                }
            }
        }
    }

    fun onAction(action: UpsertAction) {
        when (action) {
            is UpsertAction.CellValue -> {
                cellValue(column = action.columnName, value = action.newValue)
            }
            UpsertAction.DeleteDialog -> {
                _state.update { currentState ->
                    currentState.copy(isDeleteDialogVisible = !currentState.isDeleteDialogVisible)
                }
            }
            UpsertAction.Delete -> {
                delete()
            }
            UpsertAction.Upsert -> {
                upsert()
            }
        }
    }

    private fun cellValue(column: String, value: String) {
        val updatedCells = _state.value.cells.map { cellModel ->
            if (cellModel.columnName == column) {
                cellModel.copy(value = value)
            } else {
                cellModel
            }
        }
        _state.update { currentState ->
            currentState.copy(cells = updatedCells)
        }
    }

    private fun delete() {
        viewModelScope.launch {
            databaseRepository.deleteRow(
                tableName = tableName,
                rows = listOf(RowModel(_state.value.cells))
            ).collect { either ->
                either.onRight { message ->
                    screenEffect.sendEffect(NavigationEffect.NavigateBack)
                }.onLeft { message ->
                    screenEffect.sendEffect(NavigationEffect.ShowSnackBar(message))
                }
            }
        }
    }

    private fun upsert() {
        viewModelScope.launch {
            databaseRepository.upsertRow(
                tableName = tableName,
                cells = _state.value.cells
            ).collect { either ->
                either.onRight {
                    screenEffect.sendEffect(NavigationEffect.NavigateBack)
                }.onLeft { message ->
                    screenEffect.sendEffect(NavigationEffect.ShowSnackBar(message))
                }
            }
        }
    }
}