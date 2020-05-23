package com.android.basics.features.data.source.remote

import com.android.basics.features.Constants
import com.android.basics.features.data.source.TodoDataSource
import com.android.basics.features.domain.model.Result
import com.android.basics.features.domain.model.Todo
import kotlinx.coroutines.delay
import java.util.*
import kotlin.collections.LinkedHashMap

object TodoRemoteDataSource : TodoDataSource {

    private const val SERVICE_LATENCY_IN_MILLIS = 2000L

    private var TODO_SERVICE_DATA = LinkedHashMap<String, Todo>(2)

    override suspend fun getTodoList(userId: String): Result<List<Todo>> {
        // Simulate network by delaying the execution.
        val tasks = TODO_SERVICE_DATA.values.toList()
        delay(SERVICE_LATENCY_IN_MILLIS)
        return Result.Success(tasks)
    }

    override suspend fun getTodo(todoId: String): Result<Todo> {
        // Simulate network by delaying the execution.
        delay(SERVICE_LATENCY_IN_MILLIS)
        TODO_SERVICE_DATA[todoId]?.let {
            return Result.Success(it)
        }
        return Result.Error(Exception(Constants.TODO_NOT_FOUND))
    }

    override suspend fun editTodo(todo: Todo): Result<Boolean> {
        val newTodo = Todo(
            todo.todoId,
            todo.userId,
            todo.name,
            todo.description,
            todo.dueDate,
            todo.isCompleted
        )
        TODO_SERVICE_DATA[todo.todoId] = newTodo
        delay(SERVICE_LATENCY_IN_MILLIS)
        return Result.Success(true)
    }

    override suspend fun addTodo(todo: Todo): Result<Boolean> {
        val newTodo = Todo(
            UUID.randomUUID().toString(),
            todo.userId,
            todo.name,
            todo.description,
            todo.dueDate,
            todo.isCompleted
        )
        TODO_SERVICE_DATA[newTodo.todoId] = newTodo
        delay(SERVICE_LATENCY_IN_MILLIS)
        return Result.Success(true)
    }

    override suspend fun deleteTodo(todoId: String): Result<Boolean> {
        TODO_SERVICE_DATA.remove(todoId)
        delay(SERVICE_LATENCY_IN_MILLIS)
        return Result.Success(true)
    }

    override suspend fun deleteAllTodo() {
        TODO_SERVICE_DATA.clear()
    }


}