package com.android.basics.features.domain.interactor.todo

import com.android.basics.TestDataFactory
import com.android.basics.core.exception.Failure
import com.android.basics.core.functional.Either
import com.android.basics.features.domain.model.Todo
import com.android.basics.features.domain.repository.TodoRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.amshove.kluent.shouldBe
import org.junit.Test

class GetTodoListUseCaseTest {

    val todoRepository: TodoRepository = mockk()

    val useCase: GetTodoListUseCase = GetTodoListUseCase(todoRepository)

    @Test
    fun `On Get Todos Successful`() {
        runBlocking {
            //1 Set up Test data and mock responses
            val userId = TestDataFactory.getUserId()
            val testList =
                listOf(
                    TestDataFactory.getTodo(),
                    TestDataFactory.getTodo(),
                    TestDataFactory.getTodo()
                )

            coEvery { todoRepository.getTodoList(userId) } returns Either.build { testList }

            //2 Call the Unit to be tested
            val result: Either<Failure, List<Todo>> =
                useCase.run(GetTodoListUseCase.Params(userId))

            //3 Verify behaviour and state
            coVerify { todoRepository.getTodoList(userId) }

            result.isLeft shouldBe false
            result.fold({},
                { right ->
                    right shouldBe testList
                })

        }
    }


}