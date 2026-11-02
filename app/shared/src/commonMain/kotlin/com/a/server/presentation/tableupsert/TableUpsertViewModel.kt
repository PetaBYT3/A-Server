package com.a.server.presentation.tableupsert

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.a.server.core.type.SqlDataType
import com.a.server.domain.model.sqlite.FieldModel
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
import ulid.ULID

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
                    currentState.copy(
                        tableWithFields = currentState.tableWithFields.copy(
                            table = currentState.tableWithFields.table.copy(
                                name = action.newName
                            )
                        )
                    )
                }
            }
            is TableUpsertAction.PrimaryKey -> {
                primaryKey(id = action.id)
            }
            is TableUpsertAction.FieldName -> {
                fieldName(id = action.id, name = action.newName)
            }
            is TableUpsertAction.FieldType -> {
                fieldType(id = action.id, type = action.newType)
            }
            is TableUpsertAction.RemoveField -> {
                _state.update { currentState ->
                    currentState.copy(
                        tableWithFields = currentState.tableWithFields.copy(
                            fields = currentState.tableWithFields.fields.filterNot { fieldModel ->
                                fieldModel.id == action.id
                            }
                        )
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

    private fun primaryKey(id: String) {
        val updatedFields = _state.value.tableWithFields.fields.map { fieldModel ->
            fieldModel.copy(primary = id == fieldModel.id)
        }
        _state.update { currentState ->
            currentState.copy(
                tableWithFields = currentState.tableWithFields.copy(
                    fields = updatedFields
                )
            )
        }
    }

    private fun fieldName(id: String, name: String) {
        val updatedFields = _state.value.tableWithFields.fields.map { fieldModel ->
            if (fieldModel.id == id) {
                fieldModel.copy(name = name)
            } else {
                fieldModel
            }
        }
        _state.update { currentState ->
            currentState.copy(
                tableWithFields = currentState.tableWithFields.copy(
                    fields = updatedFields
                )
            )
        }
    }

    private fun fieldType(id: String, type: SqlDataType) {
        val updatedFields = _state.value.tableWithFields.fields.map { fieldModel ->
            if (fieldModel.id == id) {
                fieldModel.copy(type = type)
            } else {
                fieldModel
            }
        }
        _state.update { currentState ->
            currentState.copy(
                tableWithFields = currentState.tableWithFields.copy(
                    fields = updatedFields
                )
            )
        }
    }

    private fun addField() {
        val defaultField = FieldModel(
            id = ULID.randomULID(),
            tableName = "",
            name = "",
            defaultValue = "",
            primary = false,
            type = SqlDataType.TEXT
        )
        _state.update { currentState ->
            currentState.copy(
                tableWithFields = currentState.tableWithFields.copy(
                    fields = currentState.tableWithFields.fields + defaultField
                )
            )
        }
    }

    private fun upsert() {
        viewModelScope.launch {
            tableRepository.upsertTable(
                tableWithFieldsModel = _state.value.tableWithFields
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