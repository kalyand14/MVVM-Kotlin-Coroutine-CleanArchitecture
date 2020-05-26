package com.android.basics.features.todo.data.source.remote

import com.android.basics.core.exception.Failure
import com.android.basics.core.functional.Either
import com.android.basics.features.Constants
import com.android.basics.features.todo.data.source.TodoDataSource
import com.android.basics.features.todo.domain.model.Todo
import java.util.*
import kotlin.collections.LinkedHashMap

object FakeTodoRemoteDataSource : TodoDataSource {

    private var TODO_SERVICE_DATA = LinkedHashMap<String, Todo>(2)

    override suspend fun getTodoList(userId: String): Either<Failure, List<Todo>> {
        // Simulate network by delaying the execution.
        val tasks = TODO_SERVICE_DATA.values.toList()
        return Either.Right(tasks)
    }

    override suspend fun getTodo(todoId: String): Either<Failure, Todo> {
        // Simulate network by delaying the execution.
        TODO_SERVICE_DATA[todoId]?.let {
            return Either.Right(it)
        }
        return Either.Left(Failure.DataError(Constants.NOT_FOUND))
    }

    override suspend fun editTodo(todo: Todo): Either<Failure, Boolean> {
        val newTodo = Todo(
            todo.todoId,
            todo.userId,
            todo.name,
            todo.description,
            todo.dueDate,
            todo.isCompleted
        )
        TODO_SERVICE_DATA[todo.todoId] = newTodo
        return Either.Right(true)
    }

    override suspend fun addTodo(todo: Todo): Either<Failure, Boolean> {
        val newTodo = Todo(
            UUID.randomUUID().toString(),
            todo.userId,
            todo.name,
            todo.description,
            todo.dueDate,
            todo.isCompleted
        )
        TODO_SERVICE_DATA[newTodo.todoId] = newTodo
        return Either.Right(true)
    }

    override suspend fun deleteTodo(todoId: String): Either<Failure, Boolean> {
        TODO_SERVICE_DATA.remove(todoId)
        return Either.Right(true)
    }

    override suspend fun deleteAllTodo() {
        TODO_SERVICE_DATA.clear()
    }

}