package com.android.basics.features.todo.data.source.remote

import com.android.basics.core.exception.Failure
import com.android.basics.core.functional.Either
import com.android.basics.features.Constants
import com.android.basics.features.todo.data.source.UserDataSource
import com.android.basics.features.todo.domain.model.User
import kotlinx.coroutines.delay
import java.util.*
import kotlin.collections.LinkedHashMap


object UserRemoteDataSource : UserDataSource {

    private const val SERVICE_LATENCY_IN_MILLIS = 2000L
    private val USER_SERVICE_DATA = LinkedHashMap<String, User>()

    override suspend fun register(user: User): Either<Failure, User> {
        val userId = UUID.randomUUID().toString()
        val registeredUser = User(userId, user.userName, user.passWord)
        USER_SERVICE_DATA[userId] = registeredUser
        delay(SERVICE_LATENCY_IN_MILLIS)
        return Either.Right(registeredUser)
    }

    override suspend fun authenticate(user: User): Either<Failure, User> {
        // Simulate network by delaying the execution.
        delay(SERVICE_LATENCY_IN_MILLIS)
        USER_SERVICE_DATA[user.userId]?.let {
            return Either.Right(it)
        }
        return Either.Left(Failure.DataError(Constants.NOT_FOUND))
    }
}