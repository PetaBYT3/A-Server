package com.a.server.presentation.rowupsert

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
class RowUpsertViewModel(
    @InjectedParam
    private val tableName: String,
    @InjectedParam
    private val rowId: String,
    private val tableRepository: TableRepository
): ViewModel() {
    private val _state = MutableStateFlow(RowUpsertState())
    val state = _state.asStateFlow()

    private val _effect = Channel<Effect>(Channel.UNLIMITED)
    val effect = _effect.receiveAsFlow()

    init {
        viewModelScope.launch {
            tableRepository.getRow(
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

    fun onAction(action: RowUpsertAction) {
        when (action) {
            is RowUpsertAction.CellValue -> {
                cellValue(column = action.columnName, value = action.newValue)
            }
            RowUpsertAction.Upsert -> {
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

    private fun upsert() {
        viewModelScope.launch {
            tableRepository.upsertRow(
                tableName = tableName,
                cells = _state.value.cells
            ).collect { either ->
                either.onRight {
                    _effect.send(Effect.NavigateBack)
                }.onLeft { message ->
                    _effect.send(Effect.ShowSnackBar(message))
                }
            }
        }
    }
}