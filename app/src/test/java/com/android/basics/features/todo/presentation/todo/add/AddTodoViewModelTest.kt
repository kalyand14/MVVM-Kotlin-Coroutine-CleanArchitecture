package com.android.basics.features.todo.presentation.todo.add

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.android.basics.TestDataFactory
import com.android.basics.core.functional.Either
import com.android.basics.core.functional.ResourceStatus
import com.android.basics.features.todo.domain.repository.TodoRepository
import com.android.basics.features.todo.presentation.components.TodoCoordinator
import com.android.basics.features.todo.scope.UserScope
import com.google.common.truth.Truth
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.verify
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
class AddTodoViewModelTest {

    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    private val dispatcher = TestCoroutineDispatcher()

    private lateinit var viewModel: AddTodoViewModel

    private val todoRepository: TodoRepository = mockk()

    private val todoCoordinator: TodoCoordinator = mockk(relaxed = true)

    private val userScope: UserScope = TestDataFactory.getUserScope()

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        viewModel =
            AddTodoViewModel(todoCoordinator, todoRepository, userScope)
    }

    @After
    fun cleanup() {
        Dispatchers.resetMain()
    }

    @Test
    fun testHomeScreen_navigation() {
        // Act
        viewModel.navigate()
        // Assert
        verify { todoCoordinator.goToHomeScreen() }
    }

    @Test
    fun testCancel_navigation() {
        // Act
        viewModel.onCancel()
        // Assert
        verify { todoCoordinator.goToHomeScreen() }
    }

    @Test
    fun testSelectData_navigation() {
        // Act
        viewModel.onSelectDate()
        // Assert
        Truth.assertThat(viewModel.showDatePickerEvent.value).isNull()
    }

    @Test
    fun givenEntryEmptyValues_shouldReturnValidationError() = dispatcher.runBlockingTest {
        viewModel.onSubmit(
            "",
            "",
            ""
        )
        Truth.assertThat(viewModel.state.value?.status).isEqualTo(ResourceStatus.ERROR)
        Truth.assertThat(viewModel.state.value?.failure)
            .isEqualTo(TestDataFactory.getTodoValidationError())
    }

    @Test
    fun givenAddNewTodo_shouldReturnSuccess() = dispatcher.runBlockingTest {

        coEvery { todoRepository.addTodo(TestDataFactory.getNewTodo()) } returns Either.Right(
            TestDataFactory.getNewTodo()
        )

        dispatcher.pauseDispatcher()

        TestDataFactory.getNewTodo()
            .let { viewModel.onSubmit(it.name!!, it.description!!, it.dueDate!!) }

        Truth.assertThat(viewModel.state.value?.status).isEqualTo(ResourceStatus.LOADING)

        dispatcher.resumeDispatcher()

        coVerify { todoRepository.addTodo(TestDataFactory.getNewTodo()) }

        Truth.assertThat(viewModel.state.value?.status).isEqualTo(ResourceStatus.SUCCESS)
    }

    @Test
    fun givenAddNewTodo_shouldReturnFailure() = dispatcher.runBlockingTest {
        // prepare mock interactions
        coEvery { todoRepository.addTodo(TestDataFactory.getNewTodo()) } returns Either.Left(
            TestDataFactory.getDataError()
        )
        // suspend the function
        dispatcher.pauseDispatcher()

        // call the unit to be tested
        TestDataFactory.getNewTodo()
            .let { viewModel.onSubmit(it.name!!, it.description!!, it.dueDate!!) }

        //verify interactions and state if necessary
        Truth.assertThat(viewModel.state.value?.status).isEqualTo(ResourceStatus.LOADING)

        // resume the function
        dispatcher.resumeDispatcher()

        //verify interactions and state if necessary
        coVerify { todoRepository.addTodo(TestDataFactory.getNewTodo()) }

        Truth.assertThat(viewModel.state.value?.status).isEqualTo(ResourceStatus.ERROR)
    }
}