package com.android.basics.features.todo.data.source.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import com.android.basics.features.todo.data.source.local.entity.TodoTbl


@Dao
interface TodoDao {
    @Query("INSERT INTO todo (todoId, userId, name, description, dueDate, isCompleted) VALUES (:todoId, :userId, :name, :description, :dueDate, :isCompleted)")
    fun insert(
        todoId: String?,
        userId: String?,
        name: String?,
        description: String?,
        dueDate: String?,
        isCompleted: Boolean
    ): Long

    @Query("DELETE FROM todo WHERE todoId =:todoId")
    fun delete(todoId: String): Int

    @Update
    fun update(todoTbl: TodoTbl): Int

    @Query("SELECT * from todo WHERE userId =:userId ORDER BY todoId DESC")
    fun getAllTodo(userId: String): List<TodoTbl>

    @Query("SELECT * from todo WHERE todoId =:todoId")
    fun getTodo(todoId: String?): TodoTbl?

    @Query("DELETE FROM todo")
    fun deleteAllTodo()
}