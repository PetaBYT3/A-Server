package com.a.server.domain.repository

import arrow.core.Either
import com.a.server.domain.model.storage.FileModel
import com.a.server.domain.model.storage.FolderModel
import com.a.server.domain.model.storage.StorageModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface StorageRepository {
    val state: StateFlow<StorageModel>

    val currentPath: StateFlow<String>
    val files: StateFlow<List<FileModel>>
    suspend fun upFolder()
    suspend fun openFolder(folderName: String)
    suspend fun getFileDetail(targetPath: String): Flow<Either<String, FileModel>>
    fun createFolder(folderName: String): Flow<Either<String, String>>

    fun getFolderList(): Flow<Either<String, List<FolderModel>>>
    fun getFolderSingle(folderName: String): Flow<Either<String, FolderModel>>
    fun upsertList(folderModels: List<FolderModel>): Flow<Either<String, String>>
    fun deleteList(folderNames: List<String>): Flow<Either<String, String>>

    fun getFileList(folderName: String): Flow<Either<String, List<FileModel>>>
}