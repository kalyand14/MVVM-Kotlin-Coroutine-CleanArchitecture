package com.android.basics.features.todo.presentation.components

import com.android.basics.core.navigation.Navigator
import com.android.basics.features.todo.presentation.home.HomeActivity
import com.android.basics.features.todo.presentation.home.HomeScreenNavigator
import com.android.basics.features.todo.presentation.login.LoginActivity
import com.android.basics.features.todo.presentation.login.LoginNavigator
import com.android.basics.features.todo.presentation.registration.RegistrationActivity
import com.android.basics.features.todo.presentation.registration.RegistrationNavigator
import com.android.basics.features.todo.presentation.splash.SplashNavigator


class TodoCoordinator(private val navigator: Navigator) : HomeScreenNavigator, SplashNavigator,
    LoginNavigator, RegistrationNavigator {

    override fun goToEditTodoScreen() {
        TODO("Not yet implemented")
    }

    override fun gotoAddTodoScreen() {
        TODO("Not yet implemented")
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