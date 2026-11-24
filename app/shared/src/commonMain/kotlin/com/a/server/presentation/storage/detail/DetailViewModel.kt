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
    private val folderName: String,
    private val storageRepository: StorageRepository
): ViewModel() {
    
    private val _state = MutableStateFlow(DetailState())
    val state = _state.asStateFlow()

    init {
        getFileList()
    }

    fun getFileList() {
        viewModelScope.launch { 
            storageRepository.getFileList(
                folderName = folderName
            ).collect { either ->
                either.onRight { fileModels ->
                    _state.update { currentState ->
                        currentState.copy(
                            fileList = fileModels,
                            isFileListLoading = false
                        )
                    }
                }.onLeft { message ->
                    _state.update { currentState ->
                        currentState.copy(
                            isFileListError = message,
                            isFileListLoading = false
                        )
                    }
                }
            }
        }
    }

    fun onAction(action: DetailAction) {

    }
    
}