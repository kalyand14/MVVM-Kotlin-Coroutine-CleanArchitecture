package com.android.basics.features.todo.data.repository

import com.android.basics.core.exception.Failure
import com.android.basics.core.functional.Either
import com.android.basics.features.todo.data.source.TodoDataSource
import com.android.basics.features.todo.domain.model.Todo
import com.android.basics.features.todo.domain.repository.TodoRepository
import kotlinx.coroutines.*
import timber.log.Timber

class TodoDataRepository(
    private val todoLocalDataSource: TodoDataSource,
    private val todoRemoteDataSource: TodoDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : TodoRepository {

    override suspend fun getTodoList(userId: String): Either<Failure, List<Todo>> {
        return when (val localResult = todoLocalDataSource.getTodoList(userId)) {
            is Either.Right -> {
                Timber.d("Retriving todo list. this list contains -> ${localResult.right.size} todos")
                localResult
            }
            is Either.Left -> {
                todoRemoteDataSource.getTodoList(userId)
            }
        }
    }

    override suspend fun getTodo(todoId: String): Either<Failure, Todo> {
        return when (val localResult = todoLocalDataSource.getTodo(todoId)) {
            is Either.Right -> {
                Timber.d("Retriving todo -> ${localResult.right}")
                localResult
            }
            is Either.Left -> {
                todoRemoteDataSource.getTodo(todoId)
            }
        }
    }

    override suspend fun editTodo(todo: Todo): Either<Failure, Boolean> {
        return when (val remoteResult = todoRemoteDataSource.editTodo(todo)) {
            is Either.Right -> {
                Timber.d("Todo edited successfully with Id -> ${todo.todoId}")
                todoLocalDataSource.editTodo(todo)
            }
            is Either.Left -> {
                remoteResult
            }
        }
    }

    override suspend fun addTodo(todo: Todo): Either<Failure, Todo> {
        return when (val remoteResult = todoRemoteDataSource.addTodo(todo)) {
            is Either.Right -> {
                Timber.d("New Todo inserted successfully with Id -> ${remoteResult.right.todoId}")
                todoLocalDataSource.addTodo(remoteResult.right)
            }
            is Either.Left -> {
                remoteResult
            }
        }
    }

    override suspend fun deleteTodo(todoId: String): Either<Failure, Boolean> {
        return when (val remoteResult = todoRemoteDataSource.deleteTodo(todoId)) {
            is Either.Right -> {
                Timber.d("New Todo inserted successfully with Id -> ${todoId}")
                todoLocalDataSource.deleteTodo(todoId)
            }
            is Either.Left -> {
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