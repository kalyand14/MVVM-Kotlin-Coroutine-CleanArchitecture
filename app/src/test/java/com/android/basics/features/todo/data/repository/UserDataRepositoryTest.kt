package com.android.basics.features.todo.data.repository

import com.android.basics.TestDataFactory
import com.android.basics.core.functional.Either
import com.android.basics.features.todo.data.source.UserDataSource
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
class UserDataRepositoryTest {

    private val dispatcher = TestCoroutineDispatcher()

    private lateinit var repository: UserDataRepository

    private val userLocalDataSource: UserDataSource = mockk()

    private val userRemoteLocalSource: UserDataSource = mockk()

    private val ioDispatcher = Dispatchers.Unconfined

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        repository = UserDataRepository(userLocalDataSource, userRemoteLocalSource, ioDispatcher)
    }

    @After
    fun cleanup() {
        Dispatchers.resetMain()
    }

    @Test
    fun givenRegister_whenFetch_shouldReturnSuccess() = dispatcher.runBlockingTest {
        // Assume
        coEvery { userRemoteLocalSource.register(TestDataFactory.getNewUser()) } returns Either.Right(
            TestDataFactory.getNewUser()
        )
        coEvery { userLocalDataSource.register(TestDataFactory.getNewUser()) } returns Either.Right(
            TestDataFactory.getNewUser()
        )

        // Act
        val result = repository.register(TestDataFactory.getNewUser())

        // Assert
        coVerify { userRemoteLocalSource.register(TestDataFactory.getNewUser()) }
        Truth.assertThat(result.isRight).isTrue()
        Truth.assertThat(result.fold({ }, { it })).isEqualTo(TestDataFactory.getNewUser())
    }

    @Test
    fun givenRegister_whenFetch_shouldReturnFailure() = dispatcher.runBlockingTest {
        // Assume
        coEvery { userRemoteLocalSource.register(TestDataFactory.getNewUser()) } returns Either.Left(
            TestDataFactory.getDataError()
        )

        // Act
        val result = repository.register(TestDataFactory.getNewUser())

        // Assert
        coVerify { userRemoteLocalSource.register(TestDataFactory.getNewUser()) }
        Truth.assertThat(result.isLeft).isTrue()
        Truth.assertThat(result.fold({ it }, { })).isEqualTo(TestDataFactory.getDataError())
    }

    @Test
    fun givenAuthenticate_whenFetch_shouldReturnSuccess() = dispatcher.runBlockingTest {
        val user = TestDataFactory.getUserScope().user!!
        // Assume
        coEvery { userLocalDataSource.authenticate(user) } returns Either.Right(
            user
        )
        // Act
        val result = repository.authenticate(user)
        // Assert
        coVerify { userLocalDataSource.authenticate(user) }
        Truth.assertThat(result.isRight).isTrue()
        Truth.assertThat(result.fold({ }, { it })).isEqualTo(user)
    }

    @Test
    fun givenAuthenticate_whenFetch_shouldReturnError() = dispatcher.runBlockingTest {
        val user = TestDataFactory.getUserScope().user!!
        // Assume
        coEvery { userLocalDataSource.authenticate(user) } returns Either.Left(
            TestDataFactory.getDataError()
        )
        // Act
        val result = repository.authenticate(user)
        // Assert
        coVerify { userLocalDataSource.authenticate(user) }
        Truth.assertThat(result.isLeft).isTrue()
        Truth.assertThat(result.fold({ it }, { })).isEqualTo(TestDataFactory.getDataError())
    }

}