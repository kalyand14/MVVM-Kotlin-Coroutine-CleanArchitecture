package com.android.basics.features.todo.presentation.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.android.basics.TestDataFactory
import com.android.basics.core.functional.Either
import com.android.basics.core.functional.ResourceStatus
import com.android.basics.features.todo.domain.repository.TodoRepository
import com.android.basics.features.todo.presentation.components.TodoCoordinator
import com.android.basics.features.todo.scope.UserScope
import com.google.common.truth.Truth.assertThat
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
class HomeScreenViewModelTest {

    companion object {
        private var WELCOME_MESSAGE = "Welcome ${TestDataFactory.getUserScope().user?.userName!!}"
    }

    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    private val dispatcher = TestCoroutineDispatcher()

    private lateinit var viewModel: HomeScreenViewModel

    private val todoRepository: TodoRepository = mockk()

    private val todoCoordinator: TodoCoordinator = mockk(relaxed = true)

    private val userScope: UserScope = TestDataFactory.getUserScope()

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        viewModel =
            HomeScreenViewModel(todoRepository, todoCoordinator, userScope)
    }

    @After
    fun cleanup() {
        Dispatchers.resetMain()
    }

    @Test
    fun testLogOut_event() {

        // Act
        viewModel.onLogout()

        // Assert
        assertThat(viewModel.loggedOutEvent.value).isNull()
    }

    @Test
    fun testLogOut_navigation() {

        // Act
        viewModel.logout()

        // Assert
        verify { todoCoordinator.goToLoginScreen() }
        assertThat(userScope.user).isNull()
    }

    @Test
    fun testAddTodo_navigation() {
        // Act
        viewModel.onAddTodo()

        // Assert
        verify { todoCoordinator.gotoAddTodoScreen() }
    }

    @Test
    fun shouldSetWelcomeMessage() {
        // Assume
        coEvery { todoRepository.getTodoList(TestDataFactory.getUserId()) } returns Either.Right(
            TestDataFactory.getTodoList()
        )

        // Act
        viewModel.onLoadTodoList()

        // Assert
        assertThat(viewModel.welcomeMessageEvent.value).isEqualTo(WELCOME_MESSAGE)
    }


    @Test
    fun givenTodoList_whenFetch_shouldReturnSuccess() = dispatcher.runBlockingTest {

        coEvery { todoRepository.getTodoList(TestDataFactory.getUserId()) } returns Either.Right(
            TestDataFactory.getTodoList()
        )

        dispatcher.pauseDispatcher()

        viewModel.onLoadTodoList()

        assertThat(viewModel.state.value?.status).isEqualTo(ResourceStatus.LOADING)

        dispatcher.resumeDispatcher()

        coVerify { todoRepository.getTodoList(TestDataFactory.getUserId()) }

        assertThat(viewModel.state.value?.status).isEqualTo(ResourceStatus.SUCCESS)
    }

    @Test
    fun givenError_whenFetch_shouldReturnFailure() = dispatcher.runBlockingTest {
        // prepare mock interactions
        coEvery { todoRepository.getTodoList(TestDataFactory.getUserId()) } returns Either.Left(
            TestDataFactory.getDataError()
        )
        // suspend the function
        dispatcher.pauseDispatcher()

        // call the unit to be tested
        viewModel.onLoadTodoList()

        //verify interactions and state if necessary
        assertThat(viewModel.state.value?.status).isEqualTo(ResourceStatus.LOADING)

        // resume the function
        dispatcher.resumeDispatcher()

        //verify interactions and state if necessary
        coVerify { todoRepository.getTodoList(TestDataFactory.getUserId()) }

        assertThat(viewModel.state.value?.status).isEqualTo(ResourceStatus.ERROR)
    }
}