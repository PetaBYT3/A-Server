package com.a.server.presentation.storage.folderlist

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
class FolderListViewModel(
    private val storageRepository: StorageRepository,
    private val screenEffect: ScreenEffect
): ViewModel() {

    private val _state = MutableStateFlow(FolderListState())
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

    fun onAction(action: FolderListAction) {
        when (action) {
            is FolderListAction.SearchQuery -> {
                _state.update { currentState ->
                    currentState.copy(
                        searchQuery = action.newQuery
                    )
                }
            }
            FolderListAction.ShowUpsertFolderDialog -> {
                _state.update { currentState ->
                    currentState.copy(
                        newFolderName = "",
                        newFolderMaxSize = 50,
                        isUpsertFolderDialogVisible = true
                    )
                }
            }
            FolderListAction.DismissUpsertFolderDialog -> {
                _state.update { currentState ->
                    currentState.copy(
                        isUpsertFolderDialogVisible = false
                    )
                }
            }
            is FolderListAction.NewFolderName -> {
                _state.update { currentState ->
                    currentState.copy(
                        newFolderName = action.newName
                    )
                }
            }
            is FolderListAction.NewFolderMaxSize -> {
                _state.update { currentState ->
                    currentState.copy(
                        newFolderMaxSize = action.newSize
                    )
                }
            }
            FolderListAction.UpsertFolder -> {
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