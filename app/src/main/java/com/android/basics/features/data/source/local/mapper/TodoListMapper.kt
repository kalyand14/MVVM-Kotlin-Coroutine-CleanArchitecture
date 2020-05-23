package com.android.basics.features.data.source.local.mapper

import com.android.basics.core.Mapper
import com.android.basics.features.data.source.local.entity.TodoTbl
import com.android.basics.features.domain.model.Todo

class TodoListMapper : Mapper<List<TodoTbl>, List<Todo>> {
    override fun convert(from: List<TodoTbl>): List<Todo> {
        return from.map {
            Todo(
                it.todoId,
                it.userId,
                it.name,
                it.description,
                it.dueDate,
                it.isCompleted
            )
        }
    }
}