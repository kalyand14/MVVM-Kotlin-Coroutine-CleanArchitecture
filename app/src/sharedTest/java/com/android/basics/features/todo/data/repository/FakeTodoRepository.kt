package com.android.basics.features.todo.data.repository

import com.android.basics.core.exception.Failure
import com.android.basics.core.functional.Either
import com.android.basics.features.Constants
import com.android.basics.features.todo.domain.model.Todo
import com.android.basics.features.todo.domain.repository.TodoRepository
import java.util.*

class FakeTodoRepository(var todoList: MutableList<Todo>? = mutableListOf()) : TodoRepository {

    private var shouldReturnError = false

    fun setReturnError(value: Boolean) {
        shouldReturnError = value
    }


    override suspend fun getTodoList(userId: String): Either<Failure, List<Todo>> {
        if (shouldReturnError) {
            return Either.Left(
                Failure.ServerError
            )
        }
        todoList?.let { return Either.Right(ArrayList(it)) }
        return Either.Left(
            Failure.DataError(Constants.NOT_FOUND)
        )
    }

    override suspend fun getTodo(todoId: String): Either<Failure, Todo> {
        if (shouldReturnError) {
            return Either.Left(
                Failure.ServerError
            )
        }
        todoList?.firstOrNull { it.todoId == todoId }?.let { return Either.Right(it) }
        return Either.Left(
            Failure.DataError(Constants.NOT_FOUND)
        )
    }

    override suspend fun editTodo(todo: Todo): Either<Failure, Boolean> {
        todoList?.firstOrNull { it.todoId == todo.todoId }?.let {
            it.userId = todo.userId
            it.name = todo.name
            it.description = todo.description
            it.dueDate = todo.dueDate
            it.isCompleted = todo.isCompleted
        }
        return Either.Right(true)
    }

    override suspend fun addTodo(todo: Todo): Either<Failure, Todo> {
        todoList?.add(todo)
        return Either.Right(todo)
    }

    override suspend fun deleteTodo(todoId: String): Either<Failure, Boolean> {
        todoList?.removeIf { it.todoId == todoId }
        return Either.Right(true)
    }

    override suspend fun deleteAllTodo() {
        todoList?.clear()
    }

}