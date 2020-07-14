package com.android.basics.core.di

import android.content.Context
import androidx.room.Room
import com.android.basics.features.todo.data.source.local.TodoDatabase
import com.android.basics.features.todo.data.source.local.dao.TodoDao
import com.android.basics.features.todo.data.source.local.dao.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object DataBaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): TodoDatabase {
        return Room.databaseBuilder(
            context,
            TodoDatabase::class.java, "TodoDatabase.db"
        ).build()
    }

    @Provides
    fun provideTodoDao(database: TodoDatabase): TodoDao {
        return database.todoDao()
    }

    @Provides
    fun provideUserDao(database: TodoDatabase): UserDao {
        return database.userDao()
    }

}