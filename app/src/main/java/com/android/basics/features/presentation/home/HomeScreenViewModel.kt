package com.android.basics.features.presentation.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.basics.core.exception.Failure
import com.android.basics.core.functional.Either
import com.android.basics.core.functional.Resource
import com.android.basics.features.domain.model.Todo
import com.android.basics.features.domain.repository.TodoRepository
import kotlinx.coroutines.launch


class HomeScreenViewModel(
    private val todoRepository: TodoRepository
) : ViewModel() {



    val state: MutableLiveData<Resource<List<Todo>>> =
        MutableLiveData<Resource<List<Todo>>>()

    public fun onLoadTodoList(userId: String) {
        state.value = Resource.loading()
        viewModelScope.launch {
            when (val result = todoRepository.getTodoList(userId)) {
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
}