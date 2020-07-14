package com.android.basics.features.todo.presentation.registration

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.basics.core.exception.Failure
import com.android.basics.core.functional.Either
import com.android.basics.core.functional.Resource
import com.android.basics.features.todo.domain.model.User
import com.android.basics.features.todo.domain.repository.UserRepository
import com.android.basics.features.todo.presentation.components.TodoCoordinator
import com.android.basics.features.todo.scope.UserScope
import kotlinx.coroutines.launch
import timber.log.Timber

class RegistrationViewModel @ViewModelInject constructor(
    private val todoCoordinator: TodoCoordinator,
    private val userScope: UserScope,
    private val userRepository: UserRepository
) : ViewModel() {

    val state: MutableLiveData<Resource<User>> =
        MutableLiveData<Resource<User>>()

    fun onRegisterClick(userName: String?, password: String?) {
        User(userName = userName, passWord = password).let {
            return@let when {
                it.isValid -> {
                    state.postValue(Resource.loading())
                    viewModelScope.launch {
                        when (val regResult = userRepository.register(it)) {
                            is Either.Right -> {
                                Timber.i("User registered successfully. Now authenticating user with id -> ${regResult.right.userId}");
                                when (val result = userRepository.authenticate(it)) {
                                    is Either.Right -> {
                                        Timber.i("User authenticated. Now navigating to home screen for user with user id -> ${regResult.right.userId}");
                                        userScope.user = result.right
                                        state.postValue(Resource.success(result.right))
                                    }
                                    is Either.Left -> state.postValue(Resource.error(result.left))
                                }
                            }
                            is Either.Left -> state.postValue(Resource.error(regResult.left))
                        }
                    }
                }
                else -> state.postValue(Resource.error(validationError()))
            }
        }

    }

    private fun validationError() =
        Failure.ValidationDataError("Incorrect username and password")

    fun onLoginClick() {
        todoCoordinator.goToLoginScreen()
    }

    fun onRegistrationSuccess() {
        todoCoordinator.goToHomeScreen()
    }
}