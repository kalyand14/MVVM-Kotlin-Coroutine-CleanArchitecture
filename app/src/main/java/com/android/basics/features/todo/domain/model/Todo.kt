package com.android.basics.features.todo.domain.model

import java.util.*

data class Todo(
    var todoId: String = UUID.randomUUID().toString(),
    var userId: String,
    var name: String?,
    var description: String?,
    var dueDate: String?,
    var isCompleted: Boolean = false
) {
    var isNotValid =
        userId.isNullOrBlank() || name.isNullOrBlank() || description.isNullOrBlank() || dueDate.isNullOrBlank()
        private set
}