package com.android.basics.features.todo.presentation.home

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.IdlingRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.android.basics.TestDataFactory
import com.android.basics.core.di.ServiceLocator
import com.android.basics.features.todo.data.repository.FakeTodoRepository
import com.android.basics.features.todo.presentation.robot.home
import com.android.basics.features.todo.scope.UserScope
import com.android.basics.util.EspressoIdlingResource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
@MediumTest
@ExperimentalCoroutinesApi
class HomeActivityTest {

    private lateinit var repository: FakeTodoRepository

    @Before
    fun setUp() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
        repository = FakeTodoRepository(TestDataFactory.getTodoList())
        ServiceLocator.todoRepository = repository
        UserScope.user = TestDataFactory.getUserScope().user
    }

    @After
    fun tearDown() = runBlockingTest {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
        ServiceLocator.resetRepository()
    }

    @Test
    fun checkErrorShown() {
        ActivityScenario.launch(HomeActivity::class.java)
        repository.setReturnError(true)
        home {
            isHomeScreen()
            isErrorShown()
        }
    }

    @Test
    fun checkTodoShownInList() {
        ActivityScenario.launch(HomeActivity::class.java)
        repository.setReturnError(false)
        home {
            isHomeScreen()
            isItemShownAt(0, "New project")
            isItemShownAt(1, "New project")
            isItemShownAt(2, "New project")
        }
    }
}