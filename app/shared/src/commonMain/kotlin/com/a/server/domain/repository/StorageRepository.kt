package com.a.server.domain.repository

import arrow.core.Either
import com.a.server.domain.model.storage.FileModel
import com.a.server.domain.model.storage.FolderModel
import kotlinx.coroutines.flow.Flow

interface StorageRepository {
    fun createFolder(folderName: String): Flow<Either<String, String>>
    fun getFolderList(): Flow<Either<String, List<FolderModel>>>
    fun getFolderSingle(folderName: String): Flow<Either<String, FolderModel>>
    fun upsertList(folderModels: List<FolderModel>): Flow<Either<String, String>>
    fun deleteList(folderNames: List<String>): Flow<Either<String, String>>

    fun getFileList(folderName: String): Flow<Either<String, List<FileModel>>>
    fun getFileSingle(filePath: String): Flow<Either<String, FileModel>>
}