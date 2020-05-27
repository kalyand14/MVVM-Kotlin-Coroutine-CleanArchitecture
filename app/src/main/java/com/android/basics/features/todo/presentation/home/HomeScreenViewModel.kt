package com.android.basics.features.todo.presentation.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.basics.core.SingleLiveEvent
import com.android.basics.core.exception.Failure
import com.android.basics.core.functional.Either
import com.android.basics.core.functional.Resource
import com.android.basics.features.todo.domain.model.Todo
import com.android.basics.features.todo.domain.repository.TodoRepository
import com.android.basics.features.todo.presentation.components.TodoCoordinator
import com.android.basics.features.todo.presentation.components.UserSession
import com.android.basics.features.todo.scope.UserComponent
import kotlinx.coroutines.launch


class HomeScreenViewModel(
    private val todoRepository: TodoRepository,
    private val coordinator: TodoCoordinator,
    private val session: UserSession,
    private val userComponent: UserComponent
) : ViewModel() {

    val loggedOutEvent = SingleLiveEvent<Void>()
    val welcomeMessageEvent = SingleLiveEvent<String>()

    val state: MutableLiveData<Resource<List<Todo>>> =
        MutableLiveData<Resource<List<Todo>>>()

    public fun onLoadTodoList() {

        welcomeMessageEvent.value = "Welcome ${session.user?.userName}"

        state.value = Resource.loading()

        viewModelScope.launch {
            when (val result = todoRepository.getTodoList(session.user?.userId!!)) {
                is Either.Right -> {
                    handleTodoList(result.right)
                }
                is Either.Left -> {
                    handleError(result.left)
                }
            }
        }
    }

    private fun handleError(failure: Failure) {
        this.state.value = Resource.error(failure)
    }

    private fun handleTodoList(movies: List<Todo>) {
        this.state.value = Resource.success(movies)
    }

    fun onLogout() {
        loggedOutEvent.call()
    }

    fun logout() {
        userComponent.end()
        coordinator.goToLoginScreen()
    }

    fun onAddTodo() {
        coordinator.gotoAddTodoScreen()
    }

}