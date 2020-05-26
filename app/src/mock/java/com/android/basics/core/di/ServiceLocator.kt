package com.android.basics.core.di

import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.room.Room
import com.android.basics.core.navigation.IntentFactory
import com.android.basics.core.navigation.NativeIntentFactory
import com.android.basics.core.navigation.Navigator
import com.android.basics.core.scope.InstanceContainer
import com.android.basics.features.todo.components.TodoCoordinator
import com.android.basics.features.todo.components.TodoSession
import com.android.basics.features.todo.data.repository.TodoDataRepository
import com.android.basics.features.todo.data.source.TodoDataSource
import com.android.basics.features.todo.data.source.local.TodoDatabase
import com.android.basics.features.todo.data.source.local.TodoLocalDataSource
import com.android.basics.features.todo.data.source.remote.FakeTodoRemoteDataSource
import com.android.basics.features.todo.domain.repository.TodoRepository
import com.android.basics.features.todo.presentation.home.components.TodoListAdapter
import com.android.basics.features.todo.scope.TodoComponent
import kotlinx.coroutines.runBlocking


object ServiceLocator {

    private val container = InstanceContainer()
    private val lock = Any()
    private var database: TodoDatabase? = null

    @Volatile
    var todoRepository: TodoRepository? = null
        @VisibleForTesting set

    fun provideTodoRepository(context: Context): TodoRepository {
        synchronized(this) {
            return todoRepository
                ?: createTodoRepository(
                    context
                )
        }
    }

    private fun createTodoRepository(context: Context): TodoRepository {
        val newRepo =
            TodoDataRepository(
                createTodoLocalDataSource(
                    context
                ), FakeTodoRemoteDataSource
            )
        todoRepository = newRepo
        return newRepo
    }

    private fun createTodoLocalDataSource(context: Context): TodoDataSource {
        val database = database
            ?: createDataBase(context)
        return TodoLocalDataSource(database.todoDao())
    }

    private fun createDataBase(context: Context): TodoDatabase {
        val result = Room.databaseBuilder(
            context.applicationContext,
            TodoDatabase::class.java, "TodoDatabase.db"
        ).build()
        database = result
        return result
    }

    @VisibleForTesting
    fun resetRepository() {
        synchronized(lock) {
            runBlocking {
                FakeTodoRemoteDataSource.deleteAllTodo()
            }
            // Clear all data to avoid test pollution.
            database?.apply {
                clearAllTables()
                close()
            }
            database = null
            todoRepository = null
        }
    }

    fun getContainer(): InstanceContainer {
        return container
    }

    fun provideIntentFactory(): IntentFactory {
        return NativeIntentFactory()
    }

    fun provideNavigator(): Navigator {
        return Navigator(provideIntentFactory())
    }

    fun provideTodoCoordinator(): TodoCoordinator {
        return TodoCoordinator(provideNavigator())
    }

    fun provideTodoListAdapter(): TodoListAdapter {
        return TodoListAdapter(
            ArrayList(),
            provideTodoCoordinator(),
            TodoSession.instance,
            TodoComponent.instance
        )
    }
}