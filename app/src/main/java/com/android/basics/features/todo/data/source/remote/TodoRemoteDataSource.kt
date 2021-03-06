package com.android.basics.features.todo.data.source.remote

import com.android.basics.core.exception.Failure
import com.android.basics.core.functional.Either
import com.android.basics.features.Constants
import com.android.basics.features.todo.data.source.TodoDataSource
import com.android.basics.features.todo.domain.model.Todo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.collections.LinkedHashMap

object TodoRemoteDataSource : TodoDataSource {

    private const val SERVICE_LATENCY_IN_MILLIS = 2000L

    private var TODO_SERVICE_DATA: MutableMap<String, Todo> = LinkedHashMap<String, Todo>(2)

    override suspend fun getTodoList(userId: String): Either<Failure, List<Todo>> =
        withContext(Dispatchers.IO) {
            // Simulate network by delaying the execution.
            val tasks = TODO_SERVICE_DATA.values.toList()
            delay(SERVICE_LATENCY_IN_MILLIS)
            return@withContext Either.Right(tasks)
        }

    override suspend fun getTodo(todoId: String): Either<Failure, Todo> =
        withContext(Dispatchers.IO) {
            // Simulate network by delaying the execution.
            delay(SERVICE_LATENCY_IN_MILLIS)
            TODO_SERVICE_DATA[todoId]?.let {
                return@let Either.Right(it)
            }
            return@withContext Either.Left(Failure.DataError(Constants.NOT_FOUND))
        }

    override suspend fun editTodo(todo: Todo): Either<Failure, Boolean> =
        withContext(Dispatchers.IO) {
            val todoId = todo.todoId!!
            val newTodo = Todo(
                todo.todoId,
                todo.userId,
                todo.name,
                todo.description,
                todo.dueDate,
                todo.isCompleted
            )
            TODO_SERVICE_DATA[todoId] = newTodo
            delay(SERVICE_LATENCY_IN_MILLIS)
            return@withContext Either.Right(true)
        }

    override suspend fun addTodo(todo: Todo): Either<Failure, Todo> = withContext(Dispatchers.IO) {
        val todoId = UUID.randomUUID().toString()
        val newTodo = Todo(
            todoId,
            todo.userId,
            todo.name,
            todo.description,
            todo.dueDate,
            todo.isCompleted
        )
        TODO_SERVICE_DATA[todoId] = newTodo
        delay(SERVICE_LATENCY_IN_MILLIS)
        return@withContext Either.Right(newTodo)
    }

    override suspend fun deleteTodo(todoId: String): Either<Failure, Boolean> =
        withContext(Dispatchers.IO) {
            TODO_SERVICE_DATA.remove(todoId)
            delay(SERVICE_LATENCY_IN_MILLIS)
            return@withContext Either.Right(true)
        }

    override suspend fun deleteAllTodo() {
        TODO_SERVICE_DATA.clear()
    }

}