package com.android.basics.features.domain.interactor.todo

import com.android.basics.core.exception.Failure
import com.android.basics.core.functional.Either
import com.android.basics.core.interactor.UseCase
import com.android.basics.features.domain.model.Todo
import com.android.basics.features.domain.repository.TodoRepository

class GetTodoListUseCase(private val todoRepository: TodoRepository) :
    UseCase<List<Todo>, String>() {
    override suspend fun run(params: String): Either<Failure, List<Todo>> {
        return todoRepository.getTodoList(params)
    }
}