package com.a.server.presentation.database.executesql

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.a.server.domain.repository.DatabaseRepository
import com.a.server.presentation.ScreenEffect
import com.a.server.presentation.util.Effect
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
class ExecuteSqlViewModel(
    private val databaseRepository: DatabaseRepository,
    private val screenEffect: ScreenEffect
): ViewModel() {
    private val _state = MutableStateFlow(ExecuteSqlState())
    val state = _state.asStateFlow()

    fun onAction(action: ExecuteSqlAction) {
        when (action) {
            is ExecuteSqlAction.SqlCommand -> {
                _state.update { currentState ->
                    currentState.copy(sqlCommand = action.newCommand)
                }
            }
            ExecuteSqlAction.Execute -> {
                execute()
            }
        }
    }

    private fun execute() {
        viewModelScope.launch {
            databaseRepository.executeSql(
                query = _state.value.sqlCommand
            ).collect { either ->
                either.onRight { message ->
                    screenEffect.sendEffect(Effect.ShowSnackBar(message))
                }.onLeft { message ->
                    screenEffect.sendEffect(Effect.ShowSnackBar(message))
                }
            }
        }
    }
}