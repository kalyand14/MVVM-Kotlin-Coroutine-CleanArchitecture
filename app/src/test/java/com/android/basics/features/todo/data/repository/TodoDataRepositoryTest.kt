package com.android.basics.features.todo.data.repository

import com.android.basics.TestDataFactory
import com.android.basics.core.functional.Either
import com.android.basics.features.todo.data.source.TodoDataSource
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
class TodoDataRepositoryTest {

    private val dispatcher = TestCoroutineDispatcher()

    private lateinit var repository: TodoDataRepository

    private val todoLocalDataSource: TodoDataSource = mockk()

    private val todoRemoteDataSource: TodoDataSource = mockk()

    private val ioDispatcher = Dispatchers.Unconfined

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        repository = TodoDataRepository(todoLocalDataSource, todoRemoteDataSource, ioDispatcher)
    }

    @After
    fun cleanup() {
        Dispatchers.resetMain()
    }

    @Test
    fun giveGetTodoList_whenFetch_shouldReturnSuccess() = dispatcher.runBlockingTest {
        // Assume
        coEvery { todoRemoteDataSource.getTodoList(TestDataFactory.getUserId()) } returns Either.Right(
            TestDataFactory.getTodoList()
        )
        coEvery { todoLocalDataSource.getTodoList(TestDataFactory.getUserId()) } returns Either.Right(
            TestDataFactory.getTodoList()
        )

        // Act
        val result = repository.getTodoList(TestDataFactory.getUserId())

        // Assert
        coVerify { todoLocalDataSource.getTodoList(TestDataFactory.getUserId()) }
        Truth.assertThat(result.isRight).isTrue()
        Truth.assertThat(result.fold({ }, { it })).isEqualTo(TestDataFactory.getTodoList())
    }

    @Test
    fun giveGetTodoList_whenFetch_shouldReturnFailure() = dispatcher.runBlockingTest {
        // Assume
        coEvery { todoRemoteDataSource.getTodoList(TestDataFactory.getUserId()) } returns Either.Left(
            TestDataFactory.getDataError()
        )
        coEvery { todoLocalDataSource.getTodoList(TestDataFactory.getUserId()) } returns Either.Left(
            TestDataFactory.getDataError()
        )

        // Act
        val result = repository.getTodoList(TestDataFactory.getUserId())

        // Assert
        coVerify { todoLocalDataSource.getTodoList(TestDataFactory.getUserId()) }
        Truth.assertThat(result.isLeft).isTrue()
        Truth.assertThat(result.fold({ it }, { })).isEqualTo(TestDataFactory.getDataError())
    }

    @Test
    fun giveGetTodo_whenFetch_shouldReturnSuccess() = dispatcher.runBlockingTest {
        val todoId = TestDataFactory.getTodoScope().todo?.todoId!!
        val todo = TestDataFactory.getNewTodo()
        // Assume
        coEvery { todoRemoteDataSource.getTodo(todoId) } returns Either.Right(
            todo
        )
        coEvery { todoLocalDataSource.getTodo(todoId) } returns Either.Right(
            todo
        )

        // Act
        val result = repository.getTodo(todoId)

        // Assert
        coVerify { todoLocalDataSource.getTodo(todoId) }
        Truth.assertThat(result.isRight).isTrue()
        Truth.assertThat(result.fold({ }, { it })).isEqualTo(todo)
    }


    @Test
    fun giveGetTodo_whenFetch_shouldReturnFailure() = dispatcher.runBlockingTest {
        val todoId = TestDataFactory.getTodoScope().todo?.todoId!!
        // Assume
        coEvery { todoRemoteDataSource.getTodo(todoId) } returns Either.Left(
            TestDataFactory.getDataError()
        )
        coEvery { todoLocalDataSource.getTodo(todoId) } returns Either.Left(
            TestDataFactory.getDataError()
        )

        // Act
        val result = repository.getTodo(todoId)

        // Assert
        coVerify { todoLocalDataSource.getTodo(todoId) }
        Truth.assertThat(result.isLeft).isTrue()
        Truth.assertThat(result.fold({ it }, { })).isEqualTo(TestDataFactory.getDataError())
    }

    @Test
    fun givenAddTodo_whenFetch_shouldReturnSuccess() = dispatcher.runBlockingTest {
        // Assume
        coEvery { todoRemoteDataSource.addTodo(TestDataFactory.getNewTodo()) } returns Either.Right(
            TestDataFactory.getNewTodo()
        )
        coEvery { todoLocalDataSource.addTodo(TestDataFactory.getNewTodo()) } returns Either.Right(
            TestDataFactory.getNewTodo()
        )

        // Act
        val result = repository.addTodo(TestDataFactory.getNewTodo())

        // Assert
        coVerify { todoRemoteDataSource.addTodo(TestDataFactory.getNewTodo()) }
        coVerify { todoLocalDataSource.addTodo(TestDataFactory.getNewTodo()) }
        Truth.assertThat(result.isRight).isTrue()
        Truth.assertThat(result.fold({ }, { it })).isEqualTo(TestDataFactory.getNewTodo())
    }

    @Test
    fun givenAddTodo_whenFetch_shouldReturnFailure() = dispatcher.runBlockingTest {
        // Assume
        coEvery { todoRemoteDataSource.addTodo(TestDataFactory.getNewTodo()) } returns Either.Left(
            TestDataFactory.getDataError()
        )

        // Act
        val result = repository.addTodo(TestDataFactory.getNewTodo())

        // Assert
        coVerify { todoRemoteDataSource.addTodo(TestDataFactory.getNewTodo()) }
        Truth.assertThat(result.isLeft).isTrue()
        Truth.assertThat(result.fold({ it }, { })).isEqualTo(TestDataFactory.getDataError())
    }

    @Test
    fun givenEditTodo_whenFetch_shouldReturnSuccess() = dispatcher.runBlockingTest {
        // Assume
        coEvery { todoRemoteDataSource.editTodo(TestDataFactory.getNewTodo()) } returns Either.Right(
            true
        )
        coEvery { todoLocalDataSource.editTodo(TestDataFactory.getNewTodo()) } returns Either.Right(
            true
        )

        // Act
        val result = repository.editTodo(TestDataFactory.getNewTodo())

        // Assert
        coVerify { todoRemoteDataSource.editTodo(TestDataFactory.getNewTodo()) }
        coVerify { todoLocalDataSource.editTodo(TestDataFactory.getNewTodo()) }
        Truth.assertThat(result.isRight).isTrue()
        Truth.assertThat(result.fold({ }, { it })).isEqualTo(true)
    }

    @Test
    fun givenEditTodo_whenFetch_shouldReturnFailure() = dispatcher.runBlockingTest {
        // Assume
        coEvery { todoRemoteDataSource.editTodo(TestDataFactory.getNewTodo()) } returns Either.Left(
            TestDataFactory.getDataError()
        )
        // Act
        val result = repository.editTodo(TestDataFactory.getNewTodo())

        // Assert
        coVerify { todoRemoteDataSource.editTodo(TestDataFactory.getNewTodo()) }
        Truth.assertThat(result.isLeft).isTrue()
        Truth.assertThat(result.fold({ it }, { })).isEqualTo(TestDataFactory.getDataError())
    }

    @Test
    fun givenDeleteTodo_whenFetch_shouldReturnSuccess() = dispatcher.runBlockingTest {
        val todoId = TestDataFactory.getTodoScope().todo?.todoId!!
        // Assume
        coEvery { todoRemoteDataSource.deleteTodo(todoId) } returns Either.Right(
            true
        )
        coEvery { todoLocalDataSource.deleteTodo(todoId) } returns Either.Right(
            true
        )

        // Act
        val result = repository.deleteTodo(todoId)

        // Assert
        coVerify { todoRemoteDataSource.deleteTodo(todoId) }
        coVerify { todoLocalDataSource.deleteTodo(todoId) }
        Truth.assertThat(result.isRight).isTrue()
        Truth.assertThat(result.fold({ }, { it })).isEqualTo(true)
    }

    @Test
    fun givenDeleteTodo_whenFetch_shouldReturnFailure() = dispatcher.runBlockingTest {
        val todoId = TestDataFactory.getTodoScope().todo?.todoId!!
        // Assume
        coEvery { todoRemoteDataSource.deleteTodo(todoId) } returns Either.Left(
            TestDataFactory.getDataError()
        )
        // Act
        val result = repository.deleteTodo(todoId)

        // Assert
        coVerify { todoRemoteDataSource.deleteTodo(todoId) }
        Truth.assertThat(result.isLeft).isTrue()
        Truth.assertThat(result.fold({ it }, { })).isEqualTo(TestDataFactory.getDataError())
    }
}