package com.android.basics.features.domain.interactor.todo

import com.android.basics.MainCoroutineRule
import com.android.basics.TestDataFactory
import com.android.basics.core.exception.Failure
import com.android.basics.core.functional.Either
import com.android.basics.features.domain.model.Todo
import com.android.basics.features.domain.repository.TodoRepository
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class GetTodoListUseCaseTest {

    private lateinit var useCase: GetTodoListUseCase

    @Mock
    private lateinit var repository: TodoRepository

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this);
        useCase = GetTodoListUseCase(repository)
    }

    @Test
    fun `On Get Todo List Successful for empty list`() = mainCoroutineRule.runBlockingTest {
        `when`(repository.getTodoList(TestDataFactory.getUserId())).thenReturn(Either.build { emptyList<Todo>() })
        useCase.run(GetTodoListUseCase.Params(TestDataFactory.getUserId()))
        verify(repository).getTodoList(TestDataFactory.getUserId())
    }

    @Test
    fun `On Get Todo List Successful for actual list`() = mainCoroutineRule.runBlockingTest {
        val testList =
            mutableListOf(
                TestDataFactory.getTodo(todoId = "1"),
                TestDataFactory.getTodo(todoId = "2"),
                TestDataFactory.getTodo(todoId = "3")
            )

        `when`(repository.getTodoList(TestDataFactory.getUserId())).thenReturn(Either.Right(testList))
        val result = useCase.run(GetTodoListUseCase.Params(TestDataFactory.getUserId()))
        verify(repository).getTodoList(TestDataFactory.getUserId())
        assertThat(result.isRight).isTrue()
        val value = result.fold({}, { it }) as List<*>
        assertThat(value.size).isEqualTo(3)

    }

    @Test
    fun `On Get Todo List failure`() = mainCoroutineRule.runBlockingTest {
        `when`(repository.getTodoList(TestDataFactory.getUserId())).thenReturn(
            Either.Left(
                Failure.DataError(
                    TestDataFactory.NOT_FOUND
                )
            )
        )
        val result = useCase.run(GetTodoListUseCase.Params(TestDataFactory.getUserId()))
        verify(repository).getTodoList(TestDataFactory.getUserId())
        assertThat(result.isLeft).isTrue()


        val value = result.fold({ it }, { }) as Failure.DataError
        assertThat(value.error).isEqualTo(TestDataFactory.NOT_FOUND)
    }

}