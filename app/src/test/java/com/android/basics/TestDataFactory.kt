package com.android.basics

import com.android.basics.core.exception.Failure
import com.android.basics.features.todo.domain.model.Todo
import com.android.basics.features.todo.domain.model.User
import com.android.basics.features.todo.scope.UserScope
import java.util.*

class TestDataFactory {

    companion object Factory {

        const val NOT_FOUND = "not found"

        fun getTodo(
            todoId: String = UUID.randomUUID().toString(),
            userId: String = getUserId(),
            name: String = "New project",
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

        fun getNewTodo() =
            Todo(
                userId = getUserId(),
                name = "New project",
                description = "complete this project",
                dueDate = "28/05/2018"
            )

        fun getNewUser() = User(null, "kalyan", "password")

        fun getUserId() = getUserScope().user?.userId!!

        fun getUserScope(): UserScope {
            UserScope.user = User("1", "kalyan", "password")
            return UserScope
        }

        fun getTodoList() = mutableListOf(
            getTodo(todoId = "1"),
            getTodo(todoId = "2"),
            getTodo(todoId = "3")
        )

        fun getDataError() = Failure.DataError(TestDataFactory.NOT_FOUND)

        fun getUserValidationError() =
            Failure.ValidationDataError("Incorrect username and password")

        fun getTodoValidationError() =
            Failure.ValidationDataError("Enter all the mandatory fields")

    }
}