package com.android.basics.features.domain.interactor.todo

import com.android.basics.core.exception.Failure
import com.android.basics.core.functional.Either
import com.android.basics.core.interactor.UseCase
import com.android.basics.features.domain.model.Todo
import com.android.basics.features.domain.repository.TodoRepository

class GetTodoListUseCase(private val todoRepository: TodoRepository) :
    UseCase<List<Todo>, GetTodoListUseCase.Params>() {
    override suspend fun run(params: Params): Either<Failure, List<Todo>> {
        return todoRepository.getTodoList(params.userId)
    }

    data class Params(val userId: String)
}