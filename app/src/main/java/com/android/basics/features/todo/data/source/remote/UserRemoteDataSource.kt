package com.android.basics.features.todo.data.source.remote

import com.android.basics.core.exception.Failure
import com.android.basics.core.functional.Either
import com.android.basics.features.Constants
import com.android.basics.features.todo.data.source.UserDataSource
import com.android.basics.features.todo.domain.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.collections.LinkedHashMap


object UserRemoteDataSource : UserDataSource {

    private const val SERVICE_LATENCY_IN_MILLIS = 2000L
    private val USER_SERVICE_DATA = LinkedHashMap<String, User>()

    override suspend fun register(user: User): Either<Failure, User> = withContext(Dispatchers.IO) {
        val userId = UUID.randomUUID().toString()
        val registeredUser = User(userId, user.userName, user.passWord)
        USER_SERVICE_DATA[userId] = registeredUser
        delay(SERVICE_LATENCY_IN_MILLIS)
        return@withContext Either.Right(registeredUser)
    }

    override suspend fun authenticate(user: User): Either<Failure, User> =
        withContext(Dispatchers.IO) {
            // Simulate network by delaying the execution.
            delay(SERVICE_LATENCY_IN_MILLIS)
            USER_SERVICE_DATA[user.userId]?.let {
                return@let Either.Right(it)
            }
            return@withContext Either.Left(Failure.DataError(Constants.NOT_FOUND))
        }
}