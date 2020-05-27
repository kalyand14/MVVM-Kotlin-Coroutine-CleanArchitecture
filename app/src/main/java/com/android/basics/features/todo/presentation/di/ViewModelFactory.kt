package com.android.basics.features.todo.presentation.di

import android.app.Application
import androidx.annotation.NonNull
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.android.basics.core.di.ServiceLocator
import com.android.basics.features.todo.presentation.components.TodoCoordinator
import com.android.basics.features.todo.presentation.components.UserSession
import com.android.basics.features.todo.presentation.home.HomeScreenViewModel
import com.android.basics.features.todo.presentation.login.LoginViewModel
import com.android.basics.features.todo.presentation.registration.RegistrationViewModel
import com.android.basics.features.todo.presentation.splash.SplashViewModel
import com.android.basics.features.todo.scope.UserComponent


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
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

    private fun provideRegistrationViewModel(): RegistrationViewModel {
        return RegistrationViewModel(
            todoCoordinator,
            UserSession.instance,
            serviceLocator.provideUserRepository(application.applicationContext)
        )
    }

    private fun provideLoginViewModel(): LoginViewModel {
        return LoginViewModel(
            todoCoordinator,
            UserSession.instance,
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
            UserSession.instance,
            UserComponent.instance
        )
    }


}