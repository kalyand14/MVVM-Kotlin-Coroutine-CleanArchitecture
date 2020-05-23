package com.android.basics.features.data.source.local

import com.android.basics.core.exception.Failure
import com.android.basics.core.functional.Either
import com.android.basics.features.Constants.NOT_FOUND
import com.android.basics.features.data.source.TodoDataSource
import com.android.basics.features.data.source.local.dao.TodoDao
import com.android.basics.features.domain.model.Todo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TodoLocalDataSource internal constructor(
    private val todoDao: TodoDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : TodoDataSource {

    private suspend fun <T, R> query(
        function: () -> T,
        transform: (T) -> R,
        predicate: (T?) -> Boolean
    ): Either<Failure, R> = withContext(ioDispatcher) {
        try {
            val response: T = function()
            if (predicate(response)) {
                return@withContext Either.Right(transform(response))
            } else {
                return@withContext Either.Left(Failure.DataError(NOT_FOUND))
            }
        } catch (e: Exception) {
            return@withContext Either.Left(Failure.Error(e))
        }
    }

    override suspend fun getTodoList(userId: String): Either<Failure, List<Todo>> = query(
        { todoDao.getAllTodo(userId) },
        { it.map { row -> row.toTodo() } },
        { result -> (result != null) }
    )


    override suspend fun getTodo(todoId: String): Either<Failure, Todo> = query(
        { todoDao.getTodo(todoId) },
        { row -> row?.toTodo()!! },
        { result -> (result != null) }
    )


    override suspend fun editTodo(input: Todo): Either<Failure, Boolean> = query(
        {
            val row = todoDao.getTodo(input.todoId)?.apply {
                todoId = input.todoId
                name = input.name
                description = input.description
                dueDate = input.dueDate
            }
            val result = row?.let { todoDao.update(it) }
            result
        },
        { true },
        { result -> (result == 1) }
    )


    override suspend fun addTodo(input: Todo): Either<Failure, Boolean> = query(
        {
            val result = todoDao.insert(
                input.todoId,
                input.userId,
                input.name,
                input.description,
                input.dueDate,
                false
            )
            result
        },
        { true },
        { result -> (result?.toInt() != 1) }
    )


    override suspend fun deleteTodo(todoId: String): Either<Failure, Boolean> = query(
        { todoDao.delete(todoId) },
        { true },
        { result -> (result != 1) }
    )

    override suspend fun deleteAllTodo() = withContext(ioDispatcher) {
        todoDao.deleteAllTodo()
    }

}