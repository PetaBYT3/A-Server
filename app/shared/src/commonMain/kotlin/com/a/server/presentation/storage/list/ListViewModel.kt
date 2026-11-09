package com.a.server.presentation.storage.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.a.server.domain.repository.SettingsRepository
import com.a.server.domain.repository.StorageRepository
import com.a.server.presentation.ScreenEffect
import com.a.server.presentation.util.Effect
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
class ListViewModel(
    private val settingsRepository: SettingsRepository,
    private val storageRepository: StorageRepository,
    private val screenEffect: ScreenEffect
): ViewModel() {
    private val _state = MutableStateFlow(ListState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            settingsRepository.storagePath.collect { storagePath ->
                _state.update { currentState ->
                    currentState.copy(
                        storagePath = storagePath
                    )
                }
            }
        }

        viewModelScope.launch {
            storageRepository.currentPath.collect { currentPath ->
                _state.update { currentState ->
                    currentState.copy(currentPath = currentPath)
                }
            }
        }

        viewModelScope.launch {
            storageRepository.files.collect { fileModels ->
                _state.update { currentState ->
                    currentState.copy(
                        isFilesLoading = false,
                        files = fileModels
                    )
                }
            }
        }
    }

    fun onAction(action: ListAction) {
        when (action) {
            ListAction.UpFolder -> {
                upFolder()
            }
            is ListAction.OpenFolder -> {
                openFolder(folderName = action.folderName)
            }
            ListAction.CreateFolderDialog -> {
                createFolderDialog()
            }
            is ListAction.FolderName -> {
                _state.update { currentState ->
                    currentState.copy(newFolderName = action.folderName)
                }
            }
            ListAction.CreateFolder -> {
                createFolder()
            }
        }
    }

    private fun upFolder() {
        viewModelScope.launch {
            storageRepository.upFolder()
        }
    }

    private fun openFolder(folderName: String) {
        viewModelScope.launch {
            storageRepository.openFolder(folderName)
        }
    }

    private fun createFolderDialog() {
        _state.update { currentState ->
            when (currentState.isCreateFolderDialogVisible) {
                true -> {
                    currentState.copy(
                        isCreateFolderDialogVisible = false
                    )
                }
                false -> {
                    currentState.copy(
                        newFolderName = "",
                        isCreateFolderDialogVisible = true
                    )
                }
            }
        }
    }

    private fun createFolder() {
        viewModelScope.launch {
            storageRepository.createFolder(
                folderName = _state.value.newFolderName
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