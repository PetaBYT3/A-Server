package com.a.server.data.repository

import arrow.core.Either
import com.a.server.core.util.catchAndDispatch
import com.a.server.data.local.room.ApplicationDatabase
import com.a.server.data.mapper.AuthenticationMapper
import com.a.server.domain.model.authentication.AuthenticationModel
import com.a.server.domain.repository.AuthenticationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.koin.core.annotation.Single

@Single
class AuthenticationRepositoryImpl(
    private val applicationDatabase: ApplicationDatabase
): AuthenticationRepository {
    override fun getList(): Flow<Either<String, List<AuthenticationModel>>> {
        return flow<Either<String, List<AuthenticationModel>>> {
            applicationDatabase.authenticationDao.getList().collect { authenticationEntities ->
                val authenticationModels = authenticationEntities.map { authenticationEntity ->
                    AuthenticationMapper.toModel(authenticationEntity)
                }
                emit(Either.Right(authenticationModels))
            }
        }.catchAndDispatch()
    }

    override fun getSingle(authenticationId: String): Flow<Either<String, AuthenticationModel>> {
        return flow {
            applicationDatabase.authenticationDao.getSingle(authenticationId).collect { authenticationEntity ->
                if (authenticationEntity != null) {
                    val authenticationModel = AuthenticationMapper.toModel(authenticationEntity)
                    emit(Either.Right(authenticationModel))
                } else {
                    emit(Either.Left("Authentication Not Found"))
                }
            }
        }.catchAndDispatch()
    }

    override fun delete(ids: Set<String>): Flow<Either<String, String>> {
        return flow<Either<String, String>> {
            applicationDatabase.authenticationDao.delete(
                ids = ids
            )
            emit(Either.Right("${ids.size} Users Deleted Successfully"))
        }.catchAndDispatch()
    }
}