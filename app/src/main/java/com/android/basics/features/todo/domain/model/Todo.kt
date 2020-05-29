package com.android.basics.features.todo.domain.model

data class Todo(
    var todoId: String? = null,
    var userId: String,
    var name: String?,
    var description: String?,
    var dueDate: String?,
    var isCompleted: Boolean = false
) {
    val isNotValid
        get() = (name.isNullOrBlank() || description.isNullOrBlank() || dueDate.isNullOrBlank())

}