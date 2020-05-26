package com.android.basics.features.todo.presentation.splash

import androidx.lifecycle.ViewModel
import com.android.basics.features.todo.presentation.components.TodoCoordinator

class SplashViewModel(val todoCoordinator: TodoCoordinator) : ViewModel() {
    fun navigate() {
        todoCoordinator.goToLoginScreen()
    }
}