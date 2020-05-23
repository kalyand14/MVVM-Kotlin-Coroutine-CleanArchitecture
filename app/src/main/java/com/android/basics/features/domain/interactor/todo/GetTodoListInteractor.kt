package com.android.basics.features.domain.interactor.todo

import com.android.basics.features.domain.model.Result
import com.android.basics.features.domain.model.Todo
import com.android.basics.features.domain.repository.TodoRepository

class GetTodoListInteractor(private val todoRepository: TodoRepository) {
    suspend operator fun invoke(
        userId: String
    ): Result<List<Todo>> {
        return todoRepository.getTodoList(userId)
    }
}