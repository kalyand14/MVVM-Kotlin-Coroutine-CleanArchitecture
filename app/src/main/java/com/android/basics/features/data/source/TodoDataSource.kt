package com.android.basics.features.data.source

import com.android.basics.features.domain.model.Result
import com.android.basics.features.domain.model.Todo

interface TodoDataSource {
    suspend fun getTodoList(userId: String): Result<List<Todo>>
    suspend fun getTodo(todoId: String): Result<Todo>
    suspend fun editTodo(todo: Todo): Result<Boolean>
    suspend fun addTodo(todo: Todo): Result<Boolean>
    suspend fun deleteTodo(todoId: String): Result<Boolean>
    suspend fun deleteAllTodo()
}