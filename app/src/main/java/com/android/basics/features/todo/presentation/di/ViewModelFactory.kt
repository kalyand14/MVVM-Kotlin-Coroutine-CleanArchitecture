package com.android.basics.features.todo.presentation.di

import android.app.Application
import androidx.annotation.NonNull
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.android.basics.core.di.ServiceLocator
import com.android.basics.features.todo.presentation.components.TodoCoordinator
import com.android.basics.features.todo.presentation.home.HomeScreenViewModel
import com.android.basics.features.todo.presentation.login.LoginViewModel
import com.android.basics.features.todo.presentation.registration.RegistrationViewModel
import com.android.basics.features.todo.presentation.splash.SplashViewModel
import com.android.basics.features.todo.presentation.todo.add.AddTodoViewModel
import com.android.basics.features.todo.presentation.todo.edit.EditTodoViewModel
import com.android.basics.features.todo.scope.TodoScope
import com.android.basics.features.todo.scope.UserScope


class ViewModelFactory(private val application: Application) :
    ViewModelProvider.Factory {
    private val serviceLocator: ServiceLocator = ServiceLocator
    private var todoCoordinator: TodoCoordinator

    init {
        todoCoordinator = serviceLocator.provideTodoCoordinator()
    }

    @NonNull
    override fun <T : ViewModel?> create(@NonNull modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SplashViewModel::class.java)) {
            return provideSplashScreenViewModel() as T
        } else if (modelClass.isAssignableFrom(HomeScreenViewModel::class.java)) {
            return provideHomeScreenViewModel() as T
        } else if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return provideLoginViewModel() as T
        } else if (modelClass.isAssignableFrom(RegistrationViewModel::class.java)) {
            return provideRegistrationViewModel() as T
        } else if (modelClass.isAssignableFrom(AddTodoViewModel::class.java)) {
            return provideAddTodoViewModel() as T
        } else if (modelClass.isAssignableFrom(EditTodoViewModel::class.java)) {
            return provideEditTodoViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

    private fun provideEditTodoViewModel(): EditTodoViewModel {
        return EditTodoViewModel(
            todoCoordinator,
            serviceLocator.provideTodoRepository(application.applicationContext),
            TodoScope
        )
    }

    private fun provideAddTodoViewModel(): AddTodoViewModel {
        return AddTodoViewModel(
            todoCoordinator,
            serviceLocator.provideTodoRepository(application.applicationContext),
            UserScope
        )
    }

    private fun provideRegistrationViewModel(): RegistrationViewModel {
        return RegistrationViewModel(
            todoCoordinator,
            UserScope,
            serviceLocator.provideUserRepository(application.applicationContext)
        )
    }

    private fun provideLoginViewModel(): LoginViewModel {
        return LoginViewModel(
            todoCoordinator,
            UserScope,
            serviceLocator.provideUserRepository(application.applicationContext)
        )
    }

    private fun provideSplashScreenViewModel(): SplashViewModel {
        return SplashViewModel(todoCoordinator)
    }

    private fun provideHomeScreenViewModel(): HomeScreenViewModel {
        return HomeScreenViewModel(
            serviceLocator.provideTodoRepository(application.applicationContext),
            todoCoordinator,
            UserScope
        )
    }


}