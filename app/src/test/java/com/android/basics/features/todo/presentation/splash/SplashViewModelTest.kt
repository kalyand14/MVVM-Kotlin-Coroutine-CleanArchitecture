package com.android.basics.features.todo.presentation.splash

import com.android.basics.features.todo.presentation.components.TodoCoordinator
import io.mockk.mockk
import io.mockk.verify
import org.junit.After
import org.junit.Before
import org.junit.Test

class SplashViewModelTest {

    private lateinit var viewModel: SplashViewModel

    private val todoCoordinator: TodoCoordinator = mockk(relaxed = true)

    @Before
    fun setUp() {

        viewModel =
            SplashViewModel(todoCoordinator)
    }

    @After
    fun cleanup() {
        //Nothing
    }


    @Test
    fun test_navigation() {
        // Act
        viewModel.navigate()
        // Assert
        verify { todoCoordinator.goToLoginScreen() }

    }
}