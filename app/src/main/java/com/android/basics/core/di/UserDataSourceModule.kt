package com.android.basics.core.di

import com.android.basics.features.todo.data.source.UserDataSource
import com.android.basics.features.todo.data.source.local.TodoDatabase
import com.android.basics.features.todo.data.source.local.UserLocalDataSource
import com.android.basics.features.todo.data.source.local.dao.UserDao
import com.android.basics.features.todo.data.source.remote.UserRemoteDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object UserDataSourceModule {

    @Singleton
    @Provides
    @Local
    fun provideUserLocalDataSource(userDao: UserDao, ioDispatcher: CoroutineDispatcher): UserDataSource {
        return UserLocalDataSource(userDao, ioDispatcher)
    }

    @Singleton
    @Provides
    @Remote
    fun provideUserRemoteDataSource() : UserDataSource {
        return UserRemoteDataSource
    }
}