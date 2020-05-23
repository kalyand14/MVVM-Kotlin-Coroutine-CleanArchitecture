package com.android.basics.features.data.source.local.mapper

import com.android.basics.core.Mapper
import com.android.basics.features.data.source.local.entity.TodoTbl
import com.android.basics.features.domain.model.Todo

class TodoMapper : Mapper<TodoTbl, Todo> {
    override fun convert(fromObj: TodoTbl): Todo {
        return Todo(
            fromObj.todoId,
            fromObj.userId,
            fromObj.name,
            fromObj.description,
            fromObj.dueDate,
            fromObj.isCompleted
        )
    }
}