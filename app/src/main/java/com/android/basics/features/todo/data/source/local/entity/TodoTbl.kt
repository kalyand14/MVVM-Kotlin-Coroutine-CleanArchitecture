package com.android.basics.features.todo.data.source.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.android.basics.features.todo.domain.model.Todo
import java.util.*

@Entity(tableName = "todo")
data class TodoTbl @JvmOverloads constructor(
    @PrimaryKey @ColumnInfo(name = "todoId") var todoId: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "userId") var userId: String = "",
    @ColumnInfo(name = "name") var name: String = "",
    @ColumnInfo(name = "description") var description: String = "",
    @ColumnInfo(name = "dueDate") var dueDate: String = "",
    @ColumnInfo(name = "isCompleted") var isCompleted: Boolean = false
) {
    fun toTodo() = Todo(
        todoId,
        userId,
        name,
        description,
        dueDate,
        isCompleted
    )
}