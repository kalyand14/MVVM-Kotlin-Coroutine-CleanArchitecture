package com.android.basics.features.todo.data.source.local

import com.android.basics.TestDataFactory
import com.android.basics.features.todo.data.source.local.dao.UserDao
import com.android.basics.features.todo.data.source.local.entity.UserTbl
import com.android.basics.features.todo.domain.model.User
import com.google.common.truth.Truth
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class UserLocalDataSourceTest {

    private val dispatcher = TestCoroutineDispatcher()

    private lateinit var dataSource: UserLocalDataSource

    private val dao: UserDao = mockk()

    private val ioDispatcher = Dispatchers.Unconfined

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        dataSource = UserLocalDataSource(dao, ioDispatcher)
    }

    @After
    fun cleanup() {
        Dispatchers.resetMain()
    }

    private fun getNewUser() = User("1", "kalyan", "password")

    private fun getNewUserEntity() = UserTbl(userId = "1", userName = "kalyan", passWord = "password")

    @Test
    fun givenRegister_whenFetch_shouldReturnSuccess() = dispatcher.runBlockingTest {
        val user = getNewUser()
        val row = getNewUserEntity()

        // Assume
        coEvery {
            dao.insert(
                user.userId,
                user.userName,
                user.passWord
            )
        } returns Unit

        coEvery {
            dao.getUser(
                user.userName,
                user.passWord
            )
        } returns row

        // Act
        val result = dataSource.register(getNewUser())

        // Assert
        coVerify { dao.insert(user.userId, user.userName, user.passWord) }
        coVerify { dao.getUser(user.userName, user.passWord) }
        Truth.assertThat(result.isRight).isTrue()
        Truth.assertThat(result.fold({ }, { it })).isEqualTo(user)
    }

    @Test
    fun givenRegister_whenFetch_shouldReturnError() = dispatcher.runBlockingTest {
        val user = getNewUser()
        val row = getNewUserEntity()

        // Assume
        coEvery {
            dao.insert(
                user.userId,
                user.userName,
                user.passWord
            )
        } returns Unit

        coEvery {
            dao.getUser(
                user.userName,
                user.passWord
            )
        } returns null

        // Act
        val result = dataSource.register(getNewUser())

        // Assert
        coVerify { dao.insert(user.userId, user.userName, user.passWord) }
        coVerify { dao.getUser(user.userName, user.passWord) }
        Truth.assertThat(result.isLeft).isTrue()
        Truth.assertThat(result.fold({ it }, { })).isEqualTo(TestDataFactory.getDataError())
    }

    @Test
    fun givenAuthenticate_whenFetch_shouldReturnSuccess() = dispatcher.runBlockingTest {
        val user = getNewUser()
        val row = getNewUserEntity()

        coEvery {
            dao.getUser(
                user.userName,
                user.passWord
            )
        } returns row

        // Act
        val result = dataSource.authenticate(getNewUser())

        // Assert
        coVerify { dao.getUser(user.userName, user.passWord) }
        Truth.assertThat(result.isRight).isTrue()
        Truth.assertThat(result.fold({ }, { it })).isEqualTo(user)
    }

    @Test
    fun givenAuthenticate_whenFetch_shouldReturnError() = dispatcher.runBlockingTest {
        val user = getNewUser()

        coEvery {
            dao.getUser(
                user.userName,
                user.passWord
            )
        } returns null

        // Act
        val result = dataSource.authenticate(getNewUser())

        // Assert
        coVerify { dao.getUser(user.userName, user.passWord) }
        Truth.assertThat(result.isLeft).isTrue()
        Truth.assertThat(result.fold({ it }, { })).isEqualTo(TestDataFactory.getDataError())
    }
}