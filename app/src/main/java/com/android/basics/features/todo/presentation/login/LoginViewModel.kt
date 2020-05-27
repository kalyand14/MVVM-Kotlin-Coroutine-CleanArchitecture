package com.android.basics.features.todo.presentation.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.basics.core.exception.Failure
import com.android.basics.core.functional.Either
import com.android.basics.core.functional.Resource
import com.android.basics.features.todo.domain.model.User
import com.android.basics.features.todo.domain.repository.UserRepository
import com.android.basics.features.todo.presentation.components.TodoCoordinator
import com.android.basics.features.todo.presentation.components.UserSession
import kotlinx.coroutines.launch
import timber.log.Timber


class LoginViewModel(
    private val todoCoordinator: TodoCoordinator,
    private val userSession: UserSession,
    private val userRepository: UserRepository
) : ViewModel() {

    val state: MutableLiveData<Resource<User>> =
        MutableLiveData<Resource<User>>()

    fun onLoginClick(userName: String?, password: String?) {
        User(userName = userName, passWord = password).let {
            return@let when {
                it.isValid -> {
                    state.postValue(Resource.loading())
                    viewModelScope.launch {
                        when (val result = userRepository.authenticate(it)) {
                            is Either.Right -> {
                                Timber.i("User authenticated. Now navigating to home screen for user with user id -> ${result.right.userId}");
                                userSession.user = result.right
                                state.postValue(Resource.success(result.right))
                                todoCoordinator.goToHomeScreen()
                            }
                            is Either.Left -> {
                                state.postValue(Resource.error(result.left))
                            }
                        }
                    }
                }
                else -> {
                    state.postValue(Resource.error(validationError()))
                }
            }
        }
    }

    private fun validationError() =
        Failure.ValidationDataError("Incorrect username and password")

    fun onRegisterClick() {
        todoCoordinator.goToRegisterScreen()
    }
}