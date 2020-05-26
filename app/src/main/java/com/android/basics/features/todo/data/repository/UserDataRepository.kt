package com.android.basics.features.todo.data.repository

import com.android.basics.core.exception.Failure
import com.android.basics.core.functional.Either
import com.android.basics.features.todo.data.source.UserDataSource
import com.android.basics.features.todo.domain.model.User
import com.android.basics.features.todo.domain.repository.UserRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class UserDataRepository(
    private val userLocalDataSource: UserDataSource,
    private val userRemoteLocalSource: UserDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO

) : UserRepository {
    override suspend fun register(user: User): Either<Failure, User> {
        val localResult = userLocalDataSource.register(user)
        return if (localResult.isRight) {
            localResult
        } else {
            userRemoteLocalSource.register(user)
        }
    }

    override suspend fun authenticate(user: User): Either<Failure, User> {
        val remoteResult = userRemoteLocalSource.authenticate(user)
        return if (remoteResult.isRight) {
            userLocalDataSource.authenticate(user)
        } else {
            remoteResult
        }

    }
}