package com.android.basics.core.di

import com.android.basics.core.navigation.IntentFactory
import com.android.basics.core.navigation.NativeIntentFactory
import com.android.basics.core.navigation.Navigator
import com.android.basics.features.todo.data.repository.TodoDataRepository
import com.android.basics.features.todo.data.repository.UserDataRepository
import com.android.basics.features.todo.data.source.TodoDataSource
import com.android.basics.features.todo.data.source.UserDataSource
import com.android.basics.features.todo.data.source.remote.TodoRemoteDataSource
import com.android.basics.features.todo.domain.repository.TodoRepository
import com.android.basics.features.todo.domain.repository.UserRepository
import com.android.basics.features.todo.scope.TodoScope
import com.android.basics.features.todo.scope.UserScope
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class ApplicationModule {

    @Singleton
    @Provides
    fun provideIoDispatcher() = Dispatchers.IO

    @Singleton
    @Provides
    fun provideUserRepository(
        @Local userLocalDataSource: UserDataSource,
        @Remote userRemoteLocalSource: UserDataSource,
        ioDispatcher: CoroutineDispatcher
    ): UserRepository {
        return UserDataRepository(userLocalDataSource, userRemoteLocalSource, ioDispatcher)
    }


    @Singleton
    @Provides
    fun provideTodoRepository(
        @Local todoLocalDataSource: TodoDataSource,
        @Remote todoRemoteDataSource: TodoDataSource,
        ioDispatcher: CoroutineDispatcher
    ): TodoRepository {
        return TodoDataRepository(todoLocalDataSource, todoRemoteDataSource, ioDispatcher)
    }

    @Provides
    fun provideIntentFactory(): IntentFactory {
        return NativeIntentFactory()
    }

    @Singleton
    @Provides
    fun provideNavigator(
        intentFactory: IntentFactory
    ): Navigator {
        return Navigator(intentFactory)
    }

    @Singleton
    @Provides
    fun provideUserScope(): UserScope {
        return UserScope
    }

    @Singleton
    @Provides
    fun provideTodoScope(): TodoScope {
        return TodoScope
    }
}