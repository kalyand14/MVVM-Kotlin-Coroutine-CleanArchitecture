package com.android.basics.core.di

import com.android.basics.features.todo.presentation.components.TodoCoordinator
import com.android.basics.features.todo.presentation.home.components.TodoListAdapter
import com.android.basics.features.todo.scope.TodoScope
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityScoped

@InstallIn(ActivityComponent::class)
@Module
class AdapterModule {

    @Provides
    @ActivityScoped
    fun provideTodoAdapter(coordinator: TodoCoordinator) : TodoListAdapter{
        return TodoListAdapter(
            ArrayList(),
            coordinator,
            TodoScope
        )
    }

}