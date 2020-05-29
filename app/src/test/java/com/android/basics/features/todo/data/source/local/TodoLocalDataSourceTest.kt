package com.android.basics.features.todo.data.source.local

import com.android.basics.TestDataFactory
import com.android.basics.features.todo.data.source.local.dao.TodoDao
import com.android.basics.features.todo.data.source.local.entity.TodoTbl
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
class TodoLocalDataSourceTest {

    private val dispatcher = TestCoroutineDispatcher()

    private lateinit var dataSource: TodoLocalDataSource

    private val dao: TodoDao = mockk()

    private val ioDispatcher = Dispatchers.Unconfined

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        dataSource = TodoLocalDataSource(dao, ioDispatcher)
    }

    @After
    fun cleanup() {
        Dispatchers.resetMain()
    }

    private fun getTodoEntity(todoId: String): TodoTbl {
        val todo = TestDataFactory.getTodoScope().todo!!
        return TodoTbl(
            todoId = todoId,
            userId = "1",
            name = todo.name,
            description = todo.description,
            dueDate = todo.dueDate
        )
    }

    private fun getTodoEntityList() = mutableListOf(
        getTodoEntity(todoId = "1"),
        getTodoEntity(todoId = "2"),
        getTodoEntity(todoId = "3")
    )

    @Test
    fun givenGetTodoList_whenFetch_shouldReturnSuccess() = dispatcher.runBlockingTest {
        val todo = TestDataFactory.getTodoScope().todo!!
        val userId = TestDataFactory.getUserId()
        // Assume
        coEvery {
            dao.getAllTodo(userId)
        } returns getTodoEntityList()

        // Act
        val result = dataSource.getTodoList(userId)

        // Assert
        coVerify {
            dao.getAllTodo(userId)
        }
        Truth.assertThat(result.isRight).isTrue()
        Truth.assertThat(result.fold({ }, { it })).isEqualTo(TestDataFactory.getTodoList())
    }

    @Test
    fun givenGetTodoList_whenFetch_shouldReturnFailure() = dispatcher.runBlockingTest {
        val userId = TestDataFactory.getUserId()
        // Assume
        coEvery {
            dao.getAllTodo(userId)
        } returns emptyList()

        // Act
        val result = dataSource.getTodoList(userId)

        // Assert
        coVerify {
            dao.getAllTodo(userId)
        }
        Truth.assertThat(result.isLeft).isTrue()
        Truth.assertThat(result.fold({ it }, { })).isEqualTo(TestDataFactory.getDataError())
    }

    @Test
    fun givenGetTodo_whenFetch_shouldReturnSuccess() = dispatcher.runBlockingTest {
        val todo = TestDataFactory.getTodoScope().todo!!
        val todoId = todo.todoId!!
        // Assume
        coEvery {
            dao.getTodo(
                todoId
            )
        } returns getTodoEntity(todoId)

        // Act
        val result = dataSource.getTodo(todoId)

        // Assert
        coVerify {
            dao.getTodo(
                todoId
            )
        }
        Truth.assertThat(result.isRight).isTrue()
        Truth.assertThat(result.fold({ }, { it })).isEqualTo(todo)
    }

    @Test
    fun givenGetTodo_whenFetch_shouldReturnFailure() = dispatcher.runBlockingTest {
        val todo = TestDataFactory.getTodoScope().todo!!
        val todoId = todo.todoId!!
        // Assume
        coEvery {
            dao.getTodo(
                todoId
            )
        } returns null

        // Act
        val result = dataSource.getTodo(todoId)

        // Assert
        coVerify {
            dao.getTodo(
                todoId
            )
        }
        Truth.assertThat(result.isLeft).isTrue()
        Truth.assertThat(result.fold({ it }, { })).isEqualTo(TestDataFactory.getDataError())
    }


    @Test
    fun givenAddTodo_whenFetch_shouldReturnSuccess() = dispatcher.runBlockingTest {
        val todo = TestDataFactory.getTodoScope().todo!!
        // Assume
        coEvery {
            dao.insert(
                todo.todoId,
                todo.userId,
                todo.name,
                todo.description,
                todo.dueDate,
                todo.isCompleted
            )
        } returns 1

        // Act
        val result = dataSource.addTodo(todo)

        // Assert
        coVerify {
            dao.insert(
                todo.todoId,
                todo.userId,
                todo.name,
                todo.description,
                todo.dueDate,
                todo.isCompleted
            )
        }
        Truth.assertThat(result.isRight).isTrue()
        Truth.assertThat(result.fold({ }, { it })).isEqualTo(todo)
    }

    @Test
    fun givenAddTodo_whenFetch_shouldReturnFailure() = dispatcher.runBlockingTest {
        val todo = TestDataFactory.getTodoScope().todo!!
        // Assume
        coEvery {
            dao.insert(
                todo.todoId,
                todo.userId,
                todo.name,
                todo.description,
                todo.dueDate,
                todo.isCompleted
            )
        } returns 0

        // Act
        val result = dataSource.addTodo(todo)

        // Assert
        coVerify {
            dao.insert(
                todo.todoId,
                todo.userId,
                todo.name,
                todo.description,
                todo.dueDate,
                todo.isCompleted
            )
        }
        Truth.assertThat(result.isLeft).isTrue()
        Truth.assertThat(result.fold({ it }, { })).isEqualTo(TestDataFactory.getDataError())
    }

    @Test
    fun givenEditTodo_whenFetch_shouldReturnSuccess() = dispatcher.runBlockingTest {
        val todo = TestDataFactory.getTodoScope().todo!!
        val todoId = todo.todoId!!
        // Assume
        coEvery {
            dao.getTodo(
                todoId
            )
        } returns getTodoEntity(todoId)

        coEvery {
            dao.update(
                getTodoEntity(todoId)
            )
        } returns 1

        // Act
        val result = dataSource.editTodo(todo)

        // Assert
        coVerify {
            dao.getTodo(
                todoId
            )
        }
        coVerify {
            dao.update(
                getTodoEntity(todoId)
            )
        }
        Truth.assertThat(result.isRight).isTrue()
        Truth.assertThat(result.fold({ }, { it })).isEqualTo(true)
    }

    @Test
    fun givenEditTodo_whenFetch_shouldReturnFailure() = dispatcher.runBlockingTest {
        val todo = TestDataFactory.getTodoScope().todo!!
        val todoId = todo.todoId!!
        // Assume
        coEvery {
            dao.getTodo(
                todo.todoId
            )
        } returns getTodoEntity(todoId)

        coEvery {
            dao.update(
                getTodoEntity(todoId)
            )
        } returns 0

        // Act
        val result = dataSource.editTodo(todo)

        // Assert
        coVerify {
            dao.getTodo(
                todoId
            )
        }
        coVerify {
            dao.update(
                getTodoEntity(todoId)
            )
        }
        Truth.assertThat(result.isLeft).isTrue()
        Truth.assertThat(result.fold({ it }, { })).isEqualTo(TestDataFactory.getDataError())
    }

    @Test
    fun givenDeleteTodo_whenFetch_shouldReturnSuccess() = dispatcher.runBlockingTest {
        val todo = TestDataFactory.getTodoScope().todo!!
        val todoId = todo.todoId!!
        // Assume
        coEvery {
            dao.delete(
                todoId
            )
        } returns 1

        // Act
        val result = dataSource.deleteTodo(todoId)

        // Assert
        coVerify {
            dao.delete(
                todoId
            )
        }
        Truth.assertThat(result.isRight).isTrue()
        Truth.assertThat(result.fold({ }, { it })).isEqualTo(true)
    }

    @Test
    fun givenDeleteTodo_whenFetch_shouldReturnFailure() = dispatcher.runBlockingTest {
        val todo = TestDataFactory.getTodoScope().todo!!
        val todoId = todo.todoId!!
        // Assume
        coEvery {
            dao.delete(
                todoId
            )
        } returns 0

        // Act
        val result = dataSource.deleteTodo(todoId)

        // Assert
        coVerify {
            dao.delete(
                todoId
            )
        }
        Truth.assertThat(result.isLeft).isTrue()
        Truth.assertThat(result.fold({ it }, { })).isEqualTo(TestDataFactory.getDataError())

    }
}