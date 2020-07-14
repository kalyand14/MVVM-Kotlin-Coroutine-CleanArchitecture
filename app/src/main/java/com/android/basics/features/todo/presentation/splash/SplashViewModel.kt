package com.android.basics.features.todo.presentation.splash

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.android.basics.features.todo.presentation.components.TodoCoordinator


class SplashViewModel @ViewModelInject constructor(val todoCoordinator: TodoCoordinator) :
    ViewModel() {
    fun navigate() {
        todoCoordinator.goToLoginScreen()
    }
}