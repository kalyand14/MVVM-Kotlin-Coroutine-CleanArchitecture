package com.android.basics.features.todo.presentation.login

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import com.android.basics.TestDataFactory
import com.android.basics.features.todo.presentation.robot.login
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class LoginActivityTest {

    @get:Rule
    val activityRule: ActivityTestRule<LoginActivity> =
        ActivityTestRule(LoginActivity::class.java)

    @Test
    fun loginMissingUserName() {
        login {
            provideActivityContext(activityRule.activity)
            setPassword(TestDataFactory.getNewUser().passWord!!)
            clickLogin()
            isValidationErrorDialogShown()
        }
    }

    @Test
    fun loginMissingPassword() {
        login {
            provideActivityContext(activityRule.activity)
            setUserName(TestDataFactory.getNewUser().userName!!)
            clickLogin()
            isValidationErrorDialogShown()
        }
    }

    @Test
    fun loginWrongPassword() {
        login {
            provideActivityContext(activityRule.activity)
            setUserName(TestDataFactory.getNewUser().userName!!)
            setPassword("wrong")
            clickLogin()
            isCredentialErrorDialogShown()
        }
    }

    @Test
    fun loginSuccess() {
        login {
            setUserName(TestDataFactory.getNewUser().userName!!)
            setPassword(TestDataFactory.getNewUser().passWord!!)
            clickLogin()
        }
    }

}