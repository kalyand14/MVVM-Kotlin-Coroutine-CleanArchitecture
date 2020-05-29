package com.android.basics.features.todo.presentation.registration

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
class RegistrationViewModelTest {
    private lateinit var viewModel: RegistrationViewModel

    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    private val dispatcher = TestCoroutineDispatcher()

    private val userRepository: UserRepository = mockk()

    private val todoCoordinator: TodoCoordinator = mockk(relaxed = true)

    private val userScope: UserScope = TestDataFactory.getUserScope()

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        viewModel = RegistrationViewModel(todoCoordinator, userScope, userRepository)
    }

    @After
    fun cleanup() {
        Dispatchers.resetMain()
    }

    @Test
    fun givenRegister_whenFetch_shouldReturnValidationError() = dispatcher.runBlockingTest {
        viewModel.onRegisterClick(
            "",
            ""
        )
        Truth.assertThat(viewModel.state.value?.status).isEqualTo(ResourceStatus.ERROR)
        Truth.assertThat(viewModel.state.value?.failure)
            .isEqualTo(TestDataFactory.getValidationError())
    }

    @Test
    fun givenRegister_whenFetch_shouldReturnSuccess() = dispatcher.runBlockingTest {

        coEvery { userRepository.register(TestDataFactory.getNewUser()) } returns Either.Right(
            TestDataFactory.getNewUser()
        )

        coEvery { userRepository.authenticate(TestDataFactory.getNewUser()) } returns Either.Right(
            TestDataFactory.getNewUser()
        )

        dispatcher.pauseDispatcher()

        viewModel.onRegisterClick(
            TestDataFactory.getNewUser().userName,
            TestDataFactory.getNewUser().passWord
        )

        Truth.assertThat(viewModel.state.value?.status).isEqualTo(ResourceStatus.LOADING)

        dispatcher.resumeDispatcher()

        coVerify { userRepository.register(TestDataFactory.getNewUser()) }

        coVerify { userRepository.authenticate(TestDataFactory.getNewUser()) }

        Truth.assertThat(userScope.user).isEqualTo(TestDataFactory.getNewUser())

        Truth.assertThat(viewModel.state.value?.status).isEqualTo(ResourceStatus.SUCCESS)
    }

    /***
     * 1. Given register failed
     * 2. Verify data error is dispatched to the ui
     * */
    @Test
    fun givenRegisterAfterAuthenticate_whenFetch_shouldReturnError() = dispatcher.runBlockingTest {

        coEvery { userRepository.register(TestDataFactory.getNewUser()) } returns Either.Left(
            TestDataFactory.getDataError()
        )

        dispatcher.pauseDispatcher()

        viewModel.onRegisterClick(
            TestDataFactory.getNewUser().userName,
            TestDataFactory.getNewUser().passWord
        )

        Truth.assertThat(viewModel.state.value?.status).isEqualTo(ResourceStatus.LOADING)

        dispatcher.resumeDispatcher()

        coVerify { userRepository.register(TestDataFactory.getNewUser()) }

        Truth.assertThat(viewModel.state.value?.status).isEqualTo(ResourceStatus.ERROR)

        Truth.assertThat(viewModel.state.value?.failure)
            .isEqualTo(TestDataFactory.getDataError())
    }

    /***
     * 1. Given register success
     *  2. But authentication failed
     * 3. Verify data error is dispatched to the ui
     * */
    @Test
    fun givenAuthenticate_whenFetch_shouldReturnError() = dispatcher.runBlockingTest {

        coEvery { userRepository.register(TestDataFactory.getNewUser()) } returns Either.Right(
            TestDataFactory.getNewUser()
        )

        coEvery { userRepository.authenticate(TestDataFactory.getNewUser()) } returns Either.Left(
            TestDataFactory.getDataError()
        )

        dispatcher.pauseDispatcher()

        viewModel.onRegisterClick(
            TestDataFactory.getNewUser().userName,
            TestDataFactory.getNewUser().passWord
        )

        Truth.assertThat(viewModel.state.value?.status).isEqualTo(ResourceStatus.LOADING)

        dispatcher.resumeDispatcher()

        coVerify { userRepository.register(TestDataFactory.getNewUser()) }

        coVerify { userRepository.authenticate(TestDataFactory.getNewUser()) }

        Truth.assertThat(viewModel.state.value?.status).isEqualTo(ResourceStatus.ERROR)

        Truth.assertThat(viewModel.state.value?.failure)
            .isEqualTo(TestDataFactory.getDataError())
    }

    @Test
    fun testOnLogin_navigation() {
        // Act
        viewModel.onLoginClick()
        // Assert
        verify { todoCoordinator.goToLoginScreen() }

    }

    @Test
    fun testOnRegistrationSuccess_navigation() {
        // Act
        viewModel.onRegistrationSuccess()
        // Assert
        verify { todoCoordinator.goToHomeScreen() }

    }

}