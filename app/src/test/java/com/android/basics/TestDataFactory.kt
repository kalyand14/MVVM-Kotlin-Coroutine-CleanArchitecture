package com.android.basics

import com.android.basics.features.domain.model.Todo
import java.util.*

class TestDataFactory {

    companion object Factory {

        fun getUserId() = "1"

        fun getTodo(
            todoId: String = UUID.randomUUID().toString(),
            userId: String = getUserId(),
            name: String = "kalyan",
            description: String = "complete this project",
            dueDate: String = "28/05/2018",
            isCompleted: Boolean = false
        ) = Todo(
            todoId = todoId,
            userId = userId,
            name = name,
            description = description,
            dueDate = dueDate,
            isCompleted = isCompleted
        )
    }
}