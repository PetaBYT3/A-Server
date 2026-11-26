package com.a.server.presentation.storage.filedetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.a.server.domain.repository.StorageRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.annotation.InjectedParam
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
class FileDetailViewModel(
    @InjectedParam
    private val filePath: String,
    private val storageRepository: StorageRepository
): ViewModel() {

    private val _state = MutableStateFlow(FileDetailState())
    val state = _state.asStateFlow()

    init {
        getFileSingle()
    }

    private fun getFileSingle() {
        viewModelScope.launch {
            storageRepository.getFileSingle(
                filePath = filePath
            ).collect { either ->
                either.onRight { fileModel ->
                    _state.update { currentState ->
                        currentState.copy(
                            file = fileModel,
                            isFileLoading = false
                        )
                    }
                }.onLeft { message ->
                    _state.update { currentState ->
                        currentState.copy(
                            isFileError = message,
                            isFileLoading = false
                        )
                    }
                }
            }
        }
    }

    fun onAction(action: FileDetailAction) {

    }

}