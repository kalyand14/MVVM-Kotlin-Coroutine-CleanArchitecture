package com.android.basics.features.todo.presentation.components

import com.android.basics.core.navigation.Navigator
import com.android.basics.features.todo.presentation.home.HomeActivity
import com.android.basics.features.todo.presentation.home.HomeScreenNavigator
import com.android.basics.features.todo.presentation.login.LoginActivity
import com.android.basics.features.todo.presentation.login.LoginNavigator
import com.android.basics.features.todo.presentation.registration.RegistrationActivity
import com.android.basics.features.todo.presentation.registration.RegistrationNavigator
import com.android.basics.features.todo.presentation.splash.SplashNavigator
import com.android.basics.features.todo.presentation.todo.add.AddTodoActivity
import com.android.basics.features.todo.presentation.todo.add.AddTodoNavigator
import com.android.basics.features.todo.presentation.todo.edit.EditTodoActivity
import com.android.basics.features.todo.presentation.todo.edit.EditTodoNavigator


class TodoCoordinator(private val navigator: Navigator) : HomeScreenNavigator, SplashNavigator,
    LoginNavigator, RegistrationNavigator, AddTodoNavigator, EditTodoNavigator {

    override fun goToEditTodoScreen() {
        val intent = navigator.createIntent(EditTodoActivity::class.java)
        navigator.launchActivity(intent)

    }

    override fun gotoAddTodoScreen() {
        val intent = navigator.createIntent(AddTodoActivity::class.java)
        navigator.launchActivity(intent)

    }

    override fun goToLoginScreen() {
        val intent = navigator.createIntent(LoginActivity::class.java)
        navigator.launchActivity(intent)
        navigator.finishActivity()
    }

    override fun goToHomeScreen() {
        val intent = navigator.createIntent(HomeActivity::class.java)
        navigator.launchActivity(intent)
        navigator.finishActivity()
    }

    override fun goToRegisterScreen() {
        val intent = navigator.createIntent(RegistrationActivity::class.java)
        navigator.launchActivity(intent)
    }


}