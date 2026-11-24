package com.a.server.presentation.storage.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.a.server.domain.model.storage.FolderModel
import com.a.server.domain.repository.StorageRepository
import com.a.server.navigation.NavigationEffect
import com.a.server.presentation.ScreenEffect
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
class ListViewModel(
    private val storageRepository: StorageRepository,
    private val screenEffect: ScreenEffect
): ViewModel() {

    private val _state = MutableStateFlow(ListState())
    val state = _state.asStateFlow()

    init {
        getFolderList()
    }

    private fun getFolderList() {
        viewModelScope.launch {
            storageRepository.getFolderList().collect { either ->
                either.onRight { folderModels ->
                    _state.update { currentState ->
                        currentState.copy(
                            folderList = folderModels,
                            isFolderListLoading = false
                        )
                    }
                }.onLeft { message ->
                    _state.update { currentState ->
                        currentState.copy(
                            isFolderListError = message,
                            isFolderListLoading = false
                        )
                    }
                }
            }
        }
    }

    fun onAction(action: ListAction) {
        when (action) {
            is ListAction.SearchQuery -> {
                _state.update { currentState ->
                    currentState.copy(
                        searchQuery = action.newQuery
                    )
                }
            }
            ListAction.UpsertFolderDialog -> {
                _state.update { currentState ->
                    currentState.copy(
                        isUpsertFolderDialogVisible = !currentState.isUpsertFolderDialogVisible
                    )
                }
            }
            is ListAction.NewFolderName -> {
                _state.update { currentState ->
                    currentState.copy(
                        newFolderName = action.newName
                    )
                }
            }
            is ListAction.NewFolderMaxSize -> {
                _state.update { currentState ->
                    currentState.copy(
                        newFolderMaxSize = action.newSize
                    )
                }
            }
            ListAction.UpsertFolder -> {
                upsertFolderList()
            }
        }
    }

    private fun upsertFolderList() {
        viewModelScope.launch {
            storageRepository.upsertList(
                folderModels = listOf(
                    FolderModel(
                        name = _state.value.newFolderName,
                        maxFileSizeMegabyte = _state.value.newFolderMaxSize
                    )
                )
            ).collect { either ->
                either.onRight { message ->
                    screenEffect.sendEffect(NavigationEffect.ShowSnackBar(message))
                }.onLeft { message ->
                    screenEffect.sendEffect(NavigationEffect.ShowSnackBar(message))
                }
            }
        }
    }

}