package com.android.basics.features.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.android.basics.features.data.source.local.dao.TodoDao
import com.android.basics.features.data.source.local.entity.TodoTbl

@Database(entities = [TodoTbl::class], version = 1, exportSchema = false)
abstract class TodoDatabase : RoomDatabase() {
    abstract fun todoDao(): TodoDao
}