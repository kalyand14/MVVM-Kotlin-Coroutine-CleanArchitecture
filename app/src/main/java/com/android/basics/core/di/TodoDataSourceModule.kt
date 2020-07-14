package com.android.basics.core.di

import com.android.basics.features.todo.data.source.TodoDataSource
import com.android.basics.features.todo.data.source.local.TodoLocalDataSource
import com.android.basics.features.todo.data.source.local.dao.TodoDao
import com.android.basics.features.todo.data.source.remote.TodoRemoteDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object TodoDataSourceModule {

    @Singleton
    @Provides
    @Local
    fun provideTodoLocalDataSource(todoDao: TodoDao, ioDispatcher: CoroutineDispatcher): TodoDataSource {
        return TodoLocalDataSource(todoDao, ioDispatcher)
    }

    @Singleton
    @Provides
    @Remote
    fun provideTodoRemoteDataSource(): TodoDataSource {
        return TodoRemoteDataSource
    }
}