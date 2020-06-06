package com.android.basics.features.todo.presentation.todo.add

import androidx.test.espresso.IdlingRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import androidx.test.rule.ActivityTestRule
import com.android.basics.TestDataFactory
import com.android.basics.core.di.ServiceLocator
import com.android.basics.features.todo.data.repository.FakeTodoRepository
import com.android.basics.features.todo.domain.repository.TodoRepository
import com.android.basics.features.todo.presentation.robot.addTodo
import com.android.basics.features.todo.scope.UserScope
import com.android.basics.util.EspressoIdlingResource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@MediumTest
@ExperimentalCoroutinesApi
class AddTodoActivityTest {

    private lateinit var repository: TodoRepository

    @get:Rule
    val activityRule: ActivityTestRule<AddTodoActivity> =
        ActivityTestRule(AddTodoActivity::class.java)

    @Before
    fun initRepository() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
        repository = FakeTodoRepository()
        ServiceLocator.todoRepository = repository
        UserScope.user = TestDataFactory.getUserScope().user
    }

    @After
    fun cleanupDb() = runBlockingTest {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
        ServiceLocator.resetRepository()
    }

    @Test
    fun checkAllFieldsAreShown() {
        addTodo {
            isAddTodoScreen()
            isNameLabelShown()
            isNameInputShown()
            isDescriptionLabelShown()
            isDescriptionInputShown()
            isDateLabelShown()
            isDateInputShown()
            isSubmitShown()
        }
    }

    @Test
    fun addTodoMissingName() {
        addTodo {
            provideActivityContext(activityRule.activity)
            setDescription(TestDataFactory.getNewTodo().description!!)
            setDate(TestDataFactory.getNewTodo().dueDate!!)
            clickSubmit()
            isValidationErrorDialogShown()
        }
    }

    @Test
    fun addTodoMissingDescription() {
        addTodo {
            provideActivityContext(activityRule.activity)
            setName(TestDataFactory.getNewTodo().name!!)
            setDate(TestDataFactory.getNewTodo().dueDate!!)
            clickSubmit()
            isValidationErrorDialogShown()
        }
    }

    @Test
    fun addTodoMissingDate() {
        addTodo {
            provideActivityContext(activityRule.activity)
            setName(TestDataFactory.getNewTodo().name!!)
            setDescription(TestDataFactory.getNewTodo().description!!)
            clickSubmit()
            isValidationErrorDialogShown()
        }
    }

    @Test
    fun addTodo() {
        addTodo {
            provideActivityContext(activityRule.activity)
            setName(TestDataFactory.getNewTodo().name!!)
            setDescription(TestDataFactory.getNewTodo().description!!)
            setDate(TestDataFactory.getNewTodo().dueDate!!)
            clickSubmit()
            isSuccessDialogShown()
        }
    }


}