package com.android.basics.features.data.repository

import com.android.basics.core.exception.Failure
import com.android.basics.core.functional.Either
import com.android.basics.features.data.source.local.TodoLocalDataSource
import com.android.basics.features.data.source.remote.TodoRemoteDataSource
import com.android.basics.features.domain.model.Todo
import com.android.basics.features.domain.repository.TodoRepository
import kotlinx.coroutines.*

class TodoRepository(
    private val todoLocalDataSource: TodoLocalDataSource,
    private val todoRemoteDataSource: TodoRemoteDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : TodoRepository {

    override suspend fun getTodoList(userId: String): Either<Failure, List<Todo>> {
        val localResult = todoLocalDataSource.getTodoList(userId)
        return if (localResult.isRight) {
            localResult
        } else {
            todoRemoteDataSource.getTodoList(userId)
        }

    }

    override suspend fun getTodo(todoId: String): Either<Failure, Todo> {
        val localResult = todoLocalDataSource.getTodo(todoId)
        return if (localResult.isRight) {
            localResult
        } else {
            todoRemoteDataSource.getTodo(todoId)
        }

    }

    override suspend fun editTodo(todo: Todo): Either<Failure, Boolean> {
        // Set app as busy while this function executes.
        val remoteResult = todoRemoteDataSource.editTodo(todo)
        return if (remoteResult.isRight) {
            todoLocalDataSource.editTodo(todo)
        } else {
            remoteResult
        }

    }

    override suspend fun addTodo(todo: Todo): Either<Failure, Boolean> {
        val remoteResult = todoRemoteDataSource.addTodo(todo)
        return if (remoteResult.isRight) {
            todoLocalDataSource.addTodo(todo)
        } else {
            remoteResult
        }
    }

    override suspend fun deleteTodo(todoId: String): Either<Failure, Boolean> {
        val remoteResult = todoRemoteDataSource.deleteTodo(todoId)
        return if (remoteResult.isRight) {
            todoLocalDataSource.deleteTodo(todoId)
        } else {
            remoteResult
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