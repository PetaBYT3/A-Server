package com.a.server.data.util

import arrow.core.Either
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn

fun <T> Flow<Either<String, T>>.catchAndDispatch(
    dispatcher: CoroutineDispatcher = Dispatchers.IO
): Flow<Either<String, T>> {
    return this.catch { throwable ->
        emit(Either.Left(throwable.message ?: "Unknown Error"))
    }.flowOn(dispatcher)
}