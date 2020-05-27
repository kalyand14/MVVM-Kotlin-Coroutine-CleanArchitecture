package com.android.basics.features.todo.data.source.local

import com.android.basics.core.data.query
import com.android.basics.core.exception.Failure
import com.android.basics.core.functional.Either
import com.android.basics.features.todo.data.source.TodoDataSource
import com.android.basics.features.todo.data.source.local.dao.TodoDao
import com.android.basics.features.todo.domain.model.Todo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TodoLocalDataSource internal constructor(
    private val todoDao: TodoDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : TodoDataSource {

    override suspend fun getTodoList(userId: String): Either<Failure, List<Todo>> = query(
        ioDispatcher,
        { todoDao.getAllTodo(userId) },
        { it.map { row -> row.toTodo() } },
        { result -> (result != null) }
    )


    override suspend fun getTodo(todoId: String): Either<Failure, Todo> = query(
        ioDispatcher,
        { todoDao.getTodo(todoId) },
        { row -> row?.toTodo()!! },
        { result -> (result != null) }
    )


    override suspend fun editTodo(input: Todo): Either<Failure, Boolean> = query(
        ioDispatcher,
        {
            val row = todoDao.getTodo(input.todoId)?.apply {
                todoId = input.todoId.toString()
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


    /***
     *  1) insert newtodo into database
     *  2) if it success then return the inserted record
     * */
    override suspend fun addTodo(input: Todo): Either<Failure, Todo> = query(
        ioDispatcher,
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
        { input },
        { result -> (result?.toInt() != 1) }
    )


    override suspend fun deleteTodo(todoId: String): Either<Failure, Boolean> = query(
        ioDispatcher,
        { todoDao.delete(todoId) },
        { true },
        { result -> (result != 1) }
    )

    override suspend fun deleteAllTodo() = withContext(ioDispatcher) {
        todoDao.deleteAllTodo()
    }

}