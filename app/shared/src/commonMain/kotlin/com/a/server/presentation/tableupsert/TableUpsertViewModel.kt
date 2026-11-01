package com.a.server.presentation.tableupsert

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.a.server.domain.model.sqlite.DataType
import com.a.server.domain.model.sqlite.FieldModel
import com.a.server.domain.model.sqlite.TableModel
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
class TableUpsertViewModel(
    @InjectedParam
    private val tableId: String,
    private val tableRepository: TableRepository
): ViewModel() {
    private val _state = MutableStateFlow(TableUpsertState())
    val state = _state.asStateFlow()

    private val _effect = Channel<Effect>(Channel.UNLIMITED)
    val effect = _effect.receiveAsFlow()

    fun onAction(action: TableUpsertAction) {
        when (action) {
            is TableUpsertAction.TableName -> {
                _state.update { currentState ->
                    currentState.copy(tableName = action.newName)
                }
            }
            is TableUpsertAction.PrimaryKey -> {
                primaryKey(index = action.index)
            }
            is TableUpsertAction.FieldName -> {
                fieldName(index = action.index, name = action.newName)
            }
            is TableUpsertAction.FieldType -> {
                fieldType(index = action.index, type = action.newType)
            }
            is TableUpsertAction.RemoveField -> {
                _state.update { currentState ->
                    currentState.copy(
                        fieldList = currentState.fieldList - currentState.fieldList[action.index]
                    )
                }
            }
            TableUpsertAction.AddField -> {
                addField()
            }
            TableUpsertAction.Upsert -> {
                upsert()
            }
        }
    }

    private fun primaryKey(index: Int) {
        val updatedFields = _state.value.fieldList.mapIndexed { fieldIndex, field ->
            field.copy(primary = (fieldIndex == index))
        }
        _state.update { currentState ->
            currentState.copy(fieldList = updatedFields)
        }
    }

    private fun fieldName(index: Int, name: String) {
        val updatedFields = _state.value.fieldList.toMutableList().apply {
            if (index in indices) {
                this[index] = this[index].copy(name = name)
            }
        }
        _state.update { currentState ->
            currentState.copy(fieldList = updatedFields)
        }
    }

    private fun fieldType(index: Int, type: DataType) {
        val updatedList = _state.value.fieldList.toMutableList().apply {
            if (index in indices) {
                this[index] = this[index].copy(type = type)
            }
        }
        _state.update { currentState ->
            currentState.copy(fieldList = updatedList)
        }
    }

    private fun addField() {
        val defaultField = FieldModel(
            primary = false,
            name = "text",
            type = DataType.TEXT
        )
        _state.update { currentState ->
            currentState.copy(fieldList = currentState.fieldList + defaultField)
        }
    }

    private fun upsert() {
        viewModelScope.launch {
            tableRepository.upsert(
                tableModel = TableModel(
                    tableName = _state.value.tableName,
                    fields = _state.value.fieldList
                )
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