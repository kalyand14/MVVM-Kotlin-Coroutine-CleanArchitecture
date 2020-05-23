package com.android.basics.features.domain.model

data class Todo(
    var todoId: String,
    var userId: String,
    var name: String,
    var description: String,
    var dueDate: String,
    var isCompleted: Boolean
)