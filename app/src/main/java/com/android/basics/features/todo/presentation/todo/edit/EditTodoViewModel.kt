package com.android.basics.features.todo.presentation.todo.edit

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
import com.android.basics.features.todo.scope.TodoScope
import kotlinx.coroutines.launch


class EditTodoViewModel(
    private val todoCoordinator: TodoCoordinator,
    private val todoRepository: TodoRepository,
    private val todoScope: TodoScope
) : ViewModel() {

    val showDatePickerEvent = SingleLiveEvent<Void>()
    val loadTodoEvent = SingleLiveEvent<Todo>()
    val editTodoState = MutableLiveData<Resource<Void>>()
    val deleteTodoState = MutableLiveData<Resource<Void>>()

    fun navigate() {
        todoCoordinator.goToHomeScreen()
    }

    fun onCancel() {
        todoCoordinator.goToHomeScreen()
    }

    fun onDelete() {
        deleteTodoState.value = Resource.loading()
        todoScope.todo?.let {
            viewModelScope.launch {
                when (val result = todoRepository.deleteTodo(it.todoId)) {
                    is Either.Right -> {
                        editTodoState.postValue(Resource.success(null))
                    }
                    is Either.Left -> editTodoState.postValue(Resource.error(result.left))
                }
            }
        }
    }

    fun onSelectDate() {
        showDatePickerEvent.call()
    }

    fun loadTodo() {
        val todo: Todo? = todoScope.todo
        loadTodoEvent.value = todo
    }

    fun onSubmit(
        name: String,
        desc: String,
        date: String
    ) {
        editTodoState.value = Resource.loading()

        todoScope.todo?.let {
            it.name = name
            it.description = desc
            it.dueDate = date
            when {
                it.isNotValid -> editTodoState.postValue(Resource.error(validationError()))
                else -> {
                    editTodoState.value = Resource.loading()
                    viewModelScope.launch {
                        when (val result = todoRepository.editTodo(it)) {
                            is Either.Right -> {
                                editTodoState.postValue(Resource.success(null))
                            }
                            is Either.Left -> editTodoState.postValue(Resource.error(result.left))
                        }
                    }
                }
            }
        }
    }

    private fun validationError() =
        Failure.ValidationDataError("Enter all the mandatory fields")

}