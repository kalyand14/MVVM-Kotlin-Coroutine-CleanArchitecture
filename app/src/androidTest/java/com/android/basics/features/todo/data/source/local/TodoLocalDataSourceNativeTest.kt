package com.android.basics.features.todo.data.source.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.android.basics.MainCoroutineRule
import com.android.basics.TestDataFactory
import com.android.basics.features.todo.domain.model.Todo
import com.google.common.truth.Truth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@MediumTest
class TodoLocalDataSourceNativeTest {

    private lateinit var localDataSource: TodoLocalDataSource
    private lateinit var database: TodoDatabase

    // Set the main coroutines dispatcher for unit testing.
    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        // using an in-memory database for testing, since it doesn't survive killing the process
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            TodoDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()

        localDataSource = TodoLocalDataSource(database.todoDao(), Dispatchers.Main)
    }

    @After
    fun cleanUp() {
        database.close()
    }

    @Test
    fun testPreConditions() {
        Truth.assertThat(localDataSource).isNotNull()
    }

    @Test
    fun add_select() = runBlockingTest {
        // GIVEN - a new task saved in the database
        val todo = TestDataFactory.getTodoScope().todo!!
        localDataSource.addTodo(todo)

        // WHEN  - Task retrieved by ID
        val result = localDataSource.getTodo(todo.todoId!!)

        // THEN - Same task is returned
        Truth.assertThat(result.isRight).isTrue()
        val value = result.fold({}, { it }) as Todo
        Truth.assertThat(value.todoId).isEqualTo(todo.todoId)
        Truth.assertThat(value.userId).isEqualTo(todo.userId)
        Truth.assertThat(value.name).isEqualTo(todo.name)
        Truth.assertThat(value.description).isEqualTo(todo.description)
        Truth.assertThat(value.dueDate).isEqualTo(todo.dueDate)

    }

    @Test
    fun edit_select() = runBlockingTest {
        // GIVEN - a new task saved in the database
        val todo = TestDataFactory.getTodoScope().todo!!
        localDataSource.addTodo(todo)

        // WHEN  - Task retrieved by ID
        val result = localDataSource.getTodo(todo.todoId!!)

        // THEN - Same task is returned
        Truth.assertThat(result.isRight).isTrue()
        val value = result.fold({}, { it }) as Todo
        Truth.assertThat(value.todoId).isEqualTo(todo.todoId)
        Truth.assertThat(value.userId).isEqualTo(todo.userId)
        Truth.assertThat(value.name).isEqualTo(todo.name)
        Truth.assertThat(value.description).isEqualTo(todo.description)
        Truth.assertThat(value.dueDate).isEqualTo(todo.dueDate)

        val updatedTodo = todo.apply {
            name = "Name-Updated"
            description = "Desc-Updated"
        }

        localDataSource.editTodo(updatedTodo)

        // WHEN  - Task retrieved by ID
        val updatedResult = localDataSource.getTodo(todo.todoId!!)

        // THEN - Same task is returned
        Truth.assertThat(result.isRight).isTrue()
        val updatedValue = updatedResult.fold({}, { it }) as Todo
        Truth.assertThat(updatedValue.todoId).isEqualTo(todo.todoId)
        Truth.assertThat(updatedValue.userId).isEqualTo(todo.userId)
        Truth.assertThat(updatedValue.name).isEqualTo("Name-Updated")
        Truth.assertThat(updatedValue.description).isEqualTo("Desc-Updated")
        Truth.assertThat(updatedValue.dueDate).isEqualTo(todo.dueDate)

    }

    @Test
    fun delete_select() = runBlockingTest {
        //Insert 3 todos
        val todo1 = TestDataFactory.getTodo(name = "project 1")
        val todo2 = TestDataFactory.getTodo(name = "project 2")
        val todo3 = TestDataFactory.getTodo(name = "project 3")
        localDataSource.addTodo(todo1)
        localDataSource.addTodo(todo2)
        localDataSource.addTodo(todo3)

        // Retrieved 1 st row  and verify the row details against the inserted value
        val select_result = localDataSource.getTodo(todo1.todoId!!)
        Truth.assertThat(select_result.isRight).isTrue()
        val value = select_result.fold({}, { it }) as Todo
        Truth.assertThat(value.todoId).isEqualTo(todo1.todoId)
        Truth.assertThat(value.userId).isEqualTo(todo1.userId)
        Truth.assertThat(value.name).isEqualTo(todo1.name)
        Truth.assertThat(value.description).isEqualTo(todo1.description)
        Truth.assertThat(value.dueDate).isEqualTo(todo1.dueDate)

        // Delete the 1st row and verify it is not available in the table
        localDataSource.deleteTodo(todo1.todoId!!)
        val delete_result = localDataSource.getTodo(todo1.todoId!!)
        Truth.assertThat(delete_result.isLeft).isTrue()

        //Now check the record count
        val list_result = localDataSource.getTodoList(TestDataFactory.getUserId())
        Truth.assertThat(list_result.isRight).isTrue()
        val list_value = list_result.fold({}, { it }) as List<Todo>
        Truth.assertThat(list_value.size).isEqualTo(2)
    }
}