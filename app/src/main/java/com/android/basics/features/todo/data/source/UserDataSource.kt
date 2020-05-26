package com.android.basics.features.todo.data.source

import com.android.basics.core.exception.Failure
import com.android.basics.core.functional.Either
import com.android.basics.features.todo.domain.model.User

interface UserDataSource {
    suspend fun register(user: User): Either<Failure, User>
    suspend fun authenticate(user: User): Either<Failure, User>
}