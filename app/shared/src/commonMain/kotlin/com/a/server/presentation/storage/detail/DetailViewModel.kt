package com.a.server.presentation.storage.detail

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
class DetailViewModel(
    @InjectedParam
    private val targetPath: String,
    private val storageRepository: StorageRepository
): ViewModel() {
    private val _state = MutableStateFlow(DetailState())
    val state = _state.asStateFlow()

    init {
        getFileDetail()
    }

    fun getFileDetail() {
        viewModelScope.launch {
            storageRepository.getFileDetail(
                targetPath = targetPath
            ).collect { either ->
                either.onRight { fileModel ->
                    _state.update { currentState ->
                        currentState.copy(
                            isFileLoading = false,
                            file = fileModel
                        )
                    }
                }.onLeft { message ->
                    _state.update { currentState ->
                        currentState.copy(
                            isFileLoading = false,
                            isFileError = message
                        )
                    }
                }
            }
        }
    }

    fun onAction(action: DetailAction) {

    }
}