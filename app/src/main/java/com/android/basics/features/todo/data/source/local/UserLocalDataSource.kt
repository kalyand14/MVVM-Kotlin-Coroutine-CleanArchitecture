package com.android.basics.features.todo.data.source.local

import com.android.basics.core.data.query
import com.android.basics.core.exception.Failure
import com.android.basics.core.functional.Either
import com.android.basics.features.todo.data.source.UserDataSource
import com.android.basics.features.todo.data.source.local.dao.UserDao
import com.android.basics.features.todo.domain.model.User
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class UserLocalDataSource(
    private val userDao: UserDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : UserDataSource {
    override suspend fun register(input: User): Either<Failure, User> = query(
        ioDispatcher,
        {
            userDao.insert(input.userId, input.userName, input.passWord)
            userDao.getUser(input.userName, input.passWord)
        },
        { row -> row?.toUser()!! },
        { result -> (result != null) }
    )

    override suspend fun authenticate(user: User): Either<Failure, User> = query(
        ioDispatcher,
        { userDao.getUser(user.userName, user.passWord) },
        { row -> row?.toUser()!! },
        { result -> (result != null) }
    )
}