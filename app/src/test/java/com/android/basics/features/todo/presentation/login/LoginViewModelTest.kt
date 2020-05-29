package com.android.basics.features.todo.presentation.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.android.basics.TestDataFactory
import com.android.basics.core.functional.Either
import com.android.basics.core.functional.ResourceStatus
import com.android.basics.features.todo.domain.repository.UserRepository
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
class LoginViewModelTest {
    private lateinit var viewModel: LoginViewModel

    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    private val dispatcher = TestCoroutineDispatcher()

    private val userRepository: UserRepository = mockk()

    private val todoCoordinator: TodoCoordinator = mockk(relaxed = true)

    private val userScope: UserScope = TestDataFactory.getUserScope()

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        viewModel = LoginViewModel(todoCoordinator, userScope, userRepository)
    }

    @After
    fun cleanup() {
        Dispatchers.resetMain()
    }

    /***
     * 1. Given user
     * 2. But authentication failed
     * 3. Verify data error is dispatched to the ui
     * */
    @Test
    fun givenRegister_whenFetch_shouldReturnValidationError() = dispatcher.runBlockingTest {
        viewModel.onLoginClick(
            "",
            ""
        )
        Truth.assertThat(viewModel.state.value?.status).isEqualTo(ResourceStatus.ERROR)
        Truth.assertThat(viewModel.state.value?.failure)
            .isEqualTo(TestDataFactory.getUserValidationError())
    }

    /***
     * 1. Given register success
     *  2. But authentication failed
     * 3. Verify data error is dispatched to the ui
     * */

    @Test
    fun givenAuthentication_whenFetch_shouldReturnSuccess() = dispatcher.runBlockingTest {


        coEvery { userRepository.authenticate(TestDataFactory.getNewUser()) } returns Either.Right(
            TestDataFactory.getNewUser()
        )

        dispatcher.pauseDispatcher()

        viewModel.onLoginClick(
            TestDataFactory.getNewUser().userName,
            TestDataFactory.getNewUser().passWord
        )

        Truth.assertThat(viewModel.state.value?.status).isEqualTo(ResourceStatus.LOADING)

        dispatcher.resumeDispatcher()

        coVerify { userRepository.authenticate(TestDataFactory.getNewUser()) }

        Truth.assertThat(userScope.user).isEqualTo(TestDataFactory.getNewUser())

        Truth.assertThat(viewModel.state.value?.status).isEqualTo(ResourceStatus.SUCCESS)

        verify { todoCoordinator.goToHomeScreen() }
    }

    /***
     * 1. Given register success
     *  2. But authentication failed
     * 3. Verify data error is dispatched to the ui
     * */
    @Test
    fun givenAuthenticate_whenFetch_shouldReturnError() = dispatcher.runBlockingTest {

        coEvery { userRepository.authenticate(TestDataFactory.getNewUser()) } returns Either.Left(
            TestDataFactory.getDataError()
        )

        dispatcher.pauseDispatcher()

        viewModel.onLoginClick(
            TestDataFactory.getNewUser().userName,
            TestDataFactory.getNewUser().passWord
        )

        Truth.assertThat(viewModel.state.value?.status).isEqualTo(ResourceStatus.LOADING)

        dispatcher.resumeDispatcher()

        coVerify { userRepository.authenticate(TestDataFactory.getNewUser()) }

        Truth.assertThat(viewModel.state.value?.status).isEqualTo(ResourceStatus.ERROR)

        Truth.assertThat(viewModel.state.value?.failure)
            .isEqualTo(TestDataFactory.getDataError())
    }

    @Test
    fun testOSingUp_navigation() {
        // Act
        viewModel.onRegisterClick()
        // Assert
        verify { todoCoordinator.goToRegisterScreen() }

    }

}