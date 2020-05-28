package com.android.basics.features.todo.data.source.local

import com.android.basics.core.data.query
import com.android.basics.core.exception.Failure
import com.android.basics.core.functional.Either
import com.android.basics.features.todo.data.source.UserDataSource
import com.android.basics.features.todo.data.source.local.dao.UserDao
import com.android.basics.features.todo.domain.model.User
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import timber.log.Timber

class UserLocalDataSource(
    private val userDao: UserDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : UserDataSource {
    override suspend fun register(input: User): Either<Failure, User> = query(
        ioDispatcher,
        {
            userDao.insert(input.userId, input.userName, input.passWord)
            Timber.d("A new record inserted successfully into User table");
            val row = userDao.getUser(input.userName, input.passWord)
            Timber.d("Selected user from User table for user - ${input.userName}");
            row
        },
        { row -> row?.toUser()!! },
        { result -> (result != null) }
    )

    override suspend fun authenticate(input: User): Either<Failure, User> = query(
        ioDispatcher,
        {
            val row = userDao.getUser(input.userName, input.passWord)
            Timber.d("Selected user from User table for user - ${input.userName}");
            row
        },
        { row -> row?.toUser()!! },
        { result -> (result != null) }
    )
}