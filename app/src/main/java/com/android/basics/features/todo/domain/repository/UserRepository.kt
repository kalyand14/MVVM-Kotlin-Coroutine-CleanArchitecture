package com.android.basics.features.todo.domain.repository

import com.android.basics.core.exception.Failure
import com.android.basics.core.functional.Either
import com.android.basics.features.todo.domain.model.User

interface UserRepository {
    suspend fun register(user: User): Either<Failure, User>
    suspend fun authenticate(user: User): Either<Failure, User>
}