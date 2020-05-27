package com.android.basics.features.todo.presentation.todo.add

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
import com.android.basics.features.todo.scope.UserScope
import kotlinx.coroutines.launch


class AddTodoViewModel(
    private val todoCoordinator: TodoCoordinator,
    private val todoRepository: TodoRepository,
    private val userScope: UserScope
) :
    ViewModel() {

    val showDatePickerEvent = SingleLiveEvent<Void>()
    val state: MutableLiveData<Resource<Void>> = MutableLiveData<Resource<Void>>()

    fun onSubmit(
        name: String,
        desc: String,
        date: String
    ) {
        Todo(
            userId = userScope.user?.userId!!,
            name = name,
            description = desc,
            dueDate = date
        ).let {
            when {
                it.isNotValid -> state.postValue(Resource.error(validationError()))
                else -> {
                    state.value = Resource.loading()
                    viewModelScope.launch {
                        when (val result = todoRepository.addTodo(it)) {
                            is Either.Right -> {
                                state.postValue(Resource.success(null))
                            }
                            is Either.Left -> state.postValue(Resource.error(result.left))
                        }
                    }
                }
            }
        }
    }

    private fun validationError() =
        Failure.ValidationDataError("Enter all the mandatory fields")

    fun navigate() {
        todoCoordinator.goToHomeScreen()
    }

    fun onCancel() {
        todoCoordinator.goToHomeScreen()
    }

    fun onSelectDate() {
        showDatePickerEvent.call()
    }

}