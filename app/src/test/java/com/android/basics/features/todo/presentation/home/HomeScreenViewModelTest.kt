package com.android.basics.features.todo.presentation.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.android.basics.TestDataFactory
import com.android.basics.core.exception.Failure
import com.android.basics.core.functional.Either
import com.android.basics.core.functional.ResourceStatus
import com.android.basics.features.todo.domain.repository.TodoRepository
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule


@ExperimentalCoroutinesApi
class HomeScreenViewModelTest {

    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    private val dispatcher = TestCoroutineDispatcher()

    private lateinit var viewModel: HomeScreenViewModel

    private val todoRepository: TodoRepository = mockk()

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        viewModel = HomeScreenViewModel(todoRepository)
    }

    @After
    fun cleanup() {
        Dispatchers.resetMain()
    }

    fun getData() = mutableListOf(
        TestDataFactory.getTodo(todoId = "1"),
        TestDataFactory.getTodo(todoId = "2"),
        TestDataFactory.getTodo(todoId = "3")
    )

    fun getError() = Failure.DataError(TestDataFactory.NOT_FOUND)

    @Test
    fun givenTodoList_whenFetch_shouldReturnSuccess() = dispatcher.runBlockingTest {

        coEvery { todoRepository.getTodoList(TestDataFactory.getUserId()) } returns Either.Right(
            getData()
        )

        dispatcher.pauseDispatcher()

        viewModel.onLoadTodoList(TestDataFactory.getUserId())

        assertThat(viewModel.state.value?.status).isEqualTo(ResourceStatus.LOADING)

        dispatcher.resumeDispatcher()

        coVerify { todoRepository.getTodoList(TestDataFactory.getUserId()) }

        assertThat(viewModel.state.value?.status).isEqualTo(ResourceStatus.SUCCESS)
    }

    @Test
    fun givenError_whenFetch_shouldReturnFailure() = dispatcher.runBlockingTest {
        // prepare mock interactions
        coEvery { todoRepository.getTodoList(TestDataFactory.getUserId()) } returns Either.Left(
            getError()
        )
        // suspend the function
        dispatcher.pauseDispatcher()

        // call the unit to be tested
        viewModel.onLoadTodoList(TestDataFactory.getUserId())

        //verify interactions and state if necessary
        assertThat(viewModel.state.value?.status).isEqualTo(ResourceStatus.LOADING)

        // resume the function
        dispatcher.resumeDispatcher()

        //verify interactions and state if necessary
        coVerify { todoRepository.getTodoList(TestDataFactory.getUserId()) }

        assertThat(viewModel.state.value?.status).isEqualTo(ResourceStatus.ERROR)
    }
}