package com.android.basics.core.di

import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.room.Room
import com.android.basics.core.navigation.IntentFactory
import com.android.basics.core.navigation.NativeIntentFactory
import com.android.basics.core.navigation.Navigator
import com.android.basics.core.scope.InstanceContainer
import com.android.basics.features.todo.data.repository.TodoDataRepository
import com.android.basics.features.todo.data.repository.UserDataRepository
import com.android.basics.features.todo.data.source.TodoDataSource
import com.android.basics.features.todo.data.source.UserDataSource
import com.android.basics.features.todo.data.source.local.TodoDatabase
import com.android.basics.features.todo.data.source.local.TodoLocalDataSource
import com.android.basics.features.todo.data.source.local.UserLocalDataSource
import com.android.basics.features.todo.data.source.remote.TodoRemoteDataSource
import com.android.basics.features.todo.data.source.remote.UserRemoteDataSource
import com.android.basics.features.todo.domain.repository.TodoRepository
import com.android.basics.features.todo.domain.repository.UserRepository
import com.android.basics.features.todo.presentation.components.TodoCoordinator
import com.android.basics.features.todo.presentation.components.TodoSession
import com.android.basics.features.todo.presentation.home.components.TodoListAdapter
import com.android.basics.features.todo.scope.TodoComponent
import kotlinx.coroutines.runBlocking

object ServiceLocator {

    private val container = InstanceContainer()
    private val lock = Any()
    private var database: TodoDatabase? = null
    private var navigator: Navigator? = null
    private var todoCoordinator: TodoCoordinator? = null

    @Volatile
    var todoRepository: TodoRepository? = null
        @VisibleForTesting set

    @Volatile
    var userRepository: UserRepository? = null
        @VisibleForTesting set

    fun provideTodoRepository(context: Context): TodoRepository {
        synchronized(this) {
            return todoRepository
                ?: createTodoRepository(
                    context
                )
        }
    }

    fun provideUserRepository(context: Context): UserRepository {
        synchronized(this) {
            return userRepository
                ?: createUserRepository(
                    context
                )
        }
    }

    private fun createUserRepository(context: Context): UserRepository {
        val newRepo =
            UserDataRepository(
                createUserLocalDataSource(
                    context
                ),
                UserRemoteDataSource
            )
        userRepository = newRepo
        return newRepo
    }

    private fun createTodoRepository(context: Context): TodoRepository {
        val newRepo =
            TodoDataRepository(
                createTodoLocalDataSource(
                    context
                ),
                TodoRemoteDataSource
            )
        todoRepository = newRepo
        return newRepo
    }

    private fun createUserLocalDataSource(context: Context): UserDataSource {
        val database = database
            ?: createDataBase(context)
        return UserLocalDataSource(database.userDao())
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
                TodoRemoteDataSource.deleteAllTodo()
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

    private fun provideIntentFactory(): IntentFactory {
        return NativeIntentFactory()
    }

    fun provideNavigator(): Navigator {
        return navigator ?: createNavigator()
    }

    private fun createNavigator(): Navigator {
        navigator = Navigator(provideIntentFactory())
        return navigator as Navigator
    }

    fun provideTodoCoordinator(): TodoCoordinator {
        return todoCoordinator ?: createTodoCoordinator()
    }

    private fun createTodoCoordinator(): TodoCoordinator {
        todoCoordinator = TodoCoordinator(provideNavigator())
        return todoCoordinator as TodoCoordinator
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