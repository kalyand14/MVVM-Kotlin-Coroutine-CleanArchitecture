package com.android.basics.features.todo.domain.repository

import com.android.basics.core.exception.Failure
import com.android.basics.core.functional.Either
import com.android.basics.features.todo.domain.model.Todo

interface TodoRepository {
    suspend fun getTodoList(userId: String): Either<Failure, List<Todo>>
    suspend fun getTodo(todoId: String): Either<Failure, Todo>
    suspend fun editTodo(todo: Todo): Either<Failure, Boolean>
    suspend fun addTodo(todo: Todo): Either<Failure, Todo>
    suspend fun deleteTodo(todoId: String): Either<Failure, Boolean>
    suspend fun deleteAllTodo()
}