package com.android.basics.features.todo.data.repository

import com.android.basics.core.exception.Failure
import com.android.basics.core.functional.Either
import com.android.basics.features.todo.data.source.UserDataSource
import com.android.basics.features.todo.domain.model.User
import com.android.basics.features.todo.domain.repository.UserRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import timber.log.Timber

class UserDataRepository(
    private val userLocalDataSource: UserDataSource,
    private val userRemoteLocalSource: UserDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO

) : UserRepository {

    /**
     * Step1: register in remote data source so that user ID will be created in the server
     * Step2: if success, insert the record in the local using the ID generated in the server
     * */
    override suspend fun register(user: User): Either<Failure, User> {
        return when (val remoteResult = userRemoteLocalSource.register(user)) {
            is Either.Right -> {
                Timber.d("User registered in server with user Id -> ${remoteResult.right.userId}")
                userLocalDataSource.register(remoteResult.right)
            }
            is Either.Left -> {
                remoteResult
            }
        }
    }

    /**
     * Step1: authenticate in local data source
     * Step2: if fails, then try to authenticate in the remote server
     * */
    override suspend fun authenticate(user: User): Either<Failure, User> {
        return when (val localResult = userLocalDataSource.authenticate(user)) {
            is Either.Right -> {
                Timber.d("User ${localResult.right.userId} authenticated successfully");
                localResult
            }
            is Either.Left -> {
                userLocalDataSource.authenticate(user)
            }
        }
    }
}