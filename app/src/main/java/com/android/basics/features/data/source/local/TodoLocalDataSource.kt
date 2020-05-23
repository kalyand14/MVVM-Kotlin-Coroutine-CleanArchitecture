package com.android.basics.features.data.source.local

import com.android.basics.features.Constants.OPERATION_FAILED
import com.android.basics.features.Constants.TODO_LIST_NOT_FOUND
import com.android.basics.features.Constants.TODO_NOT_FOUND
import com.android.basics.features.data.source.TodoDataSource
import com.android.basics.features.data.source.local.dao.TodoDao
import com.android.basics.features.data.source.local.mapper.TodoListMapper
import com.android.basics.features.data.source.local.mapper.TodoMapper
import com.android.basics.features.domain.model.Result
import com.android.basics.features.domain.model.Todo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TodoLocalDataSource internal constructor(
    private val todoDao: TodoDao,
    private val todoMapper: TodoMapper,
    private val todoListMapper: TodoListMapper,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : TodoDataSource {

    override suspend fun getTodoList(userId: String): Result<List<Todo>> =
        withContext(ioDispatcher) {
            try {
                val taskList = todoDao.getAllTodo(userId)
                if (taskList != null) {
                    return@withContext Result.Success(todoListMapper.convert(taskList))
                } else {
                    return@withContext Result.Error(Exception(TODO_NOT_FOUND))
                }
            } catch (e: Exception) {
                return@withContext Result.Error(e)
            }
        }

    override suspend fun getTodo(todoId: String): Result<Todo> = withContext(ioDispatcher) {
        try {
            val task = todoDao.getTodo(todoId)
            if (task != null) {
                return@withContext Result.Success(todoMapper.convert(task))
            } else {
                return@withContext Result.Error(Exception(TODO_LIST_NOT_FOUND))
            }
        } catch (e: Exception) {
            return@withContext Result.Error(e)
        }
    }

    override suspend fun editTodo(input: Todo): Result<Boolean> = withContext(ioDispatcher) {
        try {
            val row = todoDao.getTodo(input.todoId)?.apply {
                todoId = input.todoId
                name = input.name
                description = input.description
                dueDate = input.dueDate
            }
            val result = row?.let { todoDao.update(it) }
            if (result == 1) {
                return@withContext Result.Success(true)
            } else {
                return@withContext Result.Error(Exception(OPERATION_FAILED))
            }
        } catch (e: Exception) {
            return@withContext Result.Error(e)
        }
    }

    override suspend fun addTodo(input: Todo): Result<Boolean> = withContext(ioDispatcher) {
        try {
            val result = todoDao.insert(
                input.todoId,
                input.userId,
                input.name,
                input.description,
                input.dueDate,
                false
            )
            if (result.toInt() != 1) {
                return@withContext Result.Success(true)
            } else {
                return@withContext Result.Error(Exception(OPERATION_FAILED))
            }
        } catch (e: Exception) {
            return@withContext Result.Error(e)
        }
    }

    override suspend fun deleteTodo(todoId: String): Result<Boolean> = withContext(ioDispatcher) {
        try {
            val result = todoDao.delete(todoId)
            if (result.toInt() != 1) {
                return@withContext Result.Success(true)
            } else {
                return@withContext Result.Error(Exception(OPERATION_FAILED))
            }
        } catch (e: Exception) {
            return@withContext Result.Error(e)
        }
    }

    override suspend fun deleteAllTodo() = withContext(ioDispatcher) {
        todoDao.deleteAllTodo()
    }

}