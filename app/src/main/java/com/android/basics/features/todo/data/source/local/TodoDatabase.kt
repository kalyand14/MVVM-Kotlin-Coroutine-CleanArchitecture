package com.android.basics.features.todo.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.android.basics.features.todo.data.source.local.dao.TodoDao
import com.android.basics.features.todo.data.source.local.dao.UserDao
import com.android.basics.features.todo.data.source.local.entity.TodoTbl
import com.android.basics.features.todo.data.source.local.entity.UserTbl

@Database(entities = [TodoTbl::class, UserTbl::class], version = 1, exportSchema = false)
abstract class TodoDatabase : RoomDatabase() {
    abstract fun todoDao(): TodoDao
    abstract fun userDao(): UserDao
}