package com.a.server.presentation.storage.filelist

import com.a.server.domain.model.storage.FileModel

sealed interface FileListlAction {
    data object SelectAllFiles: FileListlAction
    data object UnselectAllFiles: FileListlAction
    data class SelectFile(val file: FileModel): FileListlAction
    data class UnselectFile(val file: FileModel): FileListlAction
}