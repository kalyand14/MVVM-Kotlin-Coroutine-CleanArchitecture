package com.android.basics

import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.room.Room
import com.android.basics.features.data.repository.TodoDataRepository
import com.android.basics.features.data.source.TodoDataSource
import com.android.basics.features.data.source.local.TodoDatabase
import com.android.basics.features.data.source.local.TodoLocalDataSource
import com.android.basics.features.data.source.remote.FakeTodoRemoteDataSource
import com.android.basics.features.domain.repository.TodoRepository
import kotlinx.coroutines.runBlocking

object ServiceLocator {

    private val lock = Any()
    private var database: TodoDatabase? = null

    @Volatile
    var todoRepository: TodoRepository? = null
        @VisibleForTesting set

    fun provideTodoRepository(context: Context): TodoRepository {
        synchronized(this) {
            return todoRepository ?: createTodoRepository(context)
        }
    }

    private fun createTodoRepository(context: Context): TodoRepository {
        val newRepo =
            TodoDataRepository(createTodoLocalDataSource(context), FakeTodoRemoteDataSource)
        todoRepository = newRepo
        return newRepo
    }

    private fun createTodoLocalDataSource(context: Context): TodoDataSource {
        val database = database ?: createDataBase(context)
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
}