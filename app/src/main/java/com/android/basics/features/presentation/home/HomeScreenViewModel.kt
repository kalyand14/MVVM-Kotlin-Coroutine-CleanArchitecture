package com.android.basics.features.presentation.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.basics.core.exception.Failure
import com.android.basics.core.functional.Resource
import com.android.basics.features.domain.interactor.todo.GetTodoListUseCase
import com.android.basics.features.domain.model.Todo
import kotlinx.coroutines.launch


class HomeScreenViewModel(
    private val getTodoListUseCase: GetTodoListUseCase
) : ViewModel() {

    val state: MutableLiveData<Resource<List<Todo>>> =
        MutableLiveData<Resource<List<Todo>>>()

    public fun onLoadTodoList(userId: String) {
        state.value = Resource.loading()
        viewModelScope.launch {
            getTodoListUseCase(userId) { it.fold(::handleError, ::handleTodoList) }
        }
    }

    private fun handleError(failure: Failure) {
        this.state.value = Resource.error(failure)
    }

    private fun handleTodoList(movies: List<Todo>) {
        this.state.value = Resource.success(movies)
    }
}