package com.android.basics.features.data.repository

import com.android.basics.features.data.source.local.TodoLocalDataSource
import com.android.basics.features.data.source.remote.TodoRemoteDataSource
import com.android.basics.features.domain.model.Result
import com.android.basics.features.domain.model.Todo
import com.android.basics.features.domain.repository.TodoRepository
import com.android.basics.util.wrapEspressoIdlingResource
import kotlinx.coroutines.*

class TodoRepository(
    private val todoLocalDataSource: TodoLocalDataSource,
    private val todoRemoteDataSource: TodoRemoteDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : TodoRepository {

    override suspend fun getTodoList(userId: String): Result<List<Todo>> {
        wrapEspressoIdlingResource {
            val localResult = todoLocalDataSource.getTodoList(userId)
            return if (localResult is Result.Success) {
                localResult
            } else {
                todoRemoteDataSource.getTodoList(userId)
            }
        }
    }

    override suspend fun getTodo(todoId: String): Result<Todo> {
        wrapEspressoIdlingResource {
            val localResult = todoLocalDataSource.getTodo(todoId)
            return if (localResult is Result.Success) {
                localResult
            } else {
                todoRemoteDataSource.getTodo(todoId)
            }
        }
    }

    override suspend fun editTodo(todo: Todo): Result<Boolean> {
        // Set app as busy while this function executes.
        wrapEspressoIdlingResource {
            val remoteResult = todoRemoteDataSource.editTodo(todo)
            return if (remoteResult is Result.Success) {
                todoLocalDataSource.editTodo(todo)
            } else {
                remoteResult
            }
        }
    }

    override suspend fun addTodo(todo: Todo): Result<Boolean> {
        wrapEspressoIdlingResource {
            val remoteResult = todoRemoteDataSource.addTodo(todo)
            return if (remoteResult is Result.Success) {
                todoLocalDataSource.addTodo(todo)
            } else {
                remoteResult
            }
        }
    }

    override suspend fun deleteTodo(todoId: String): Result<Boolean> {
        wrapEspressoIdlingResource {
            val remoteResult = todoRemoteDataSource.deleteTodo(todoId)
            return if (remoteResult is Result.Success) {
                todoLocalDataSource.deleteTodo(todoId)
            } else {
                remoteResult
            }
        }
    }

    override suspend fun deleteAllTodo() {
        withContext(ioDispatcher) {
            coroutineScope {
                launch { todoLocalDataSource.deleteAllTodo() }
                launch { todoRemoteDataSource.deleteAllTodo() }
            }
        }
    }
}