package com.android.basics.features.todo.presentation.signWith

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import com.android.basics.TestDataFactory
import com.android.basics.features.todo.presentation.registration.RegistrationActivity
import com.android.basics.features.todo.presentation.robot.login
import com.android.basics.features.todo.presentation.robot.signUp
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class RegistrationActivityTest {

    @get:Rule
    val activityRule: ActivityTestRule<RegistrationActivity> =
        ActivityTestRule(RegistrationActivity::class.java)

    @Test
    fun checkAllFieldsAreShown() {
        signUp {
            isSignUpScreen()
            isHeaderShown()
            isUserNameLabelShown()
            isPasswordLabelShown()
            isLoginButtonShown()
            isSignUpButtonShown()
            isSignUpButtonLabelShown()
        }
    }

    @Test
    fun signWithMissingUserName() {
        signUp {
            provideActivityContext(activityRule.activity)
            setPassword(TestDataFactory.getNewUser().passWord!!)
            clickSignUp()
            isValidationErrorDialogShown()
        }
    }

    @Test
    fun signWithMissingPassword() {
        signUp {
            provideActivityContext(activityRule.activity)
            setUserName(TestDataFactory.getNewUser().userName!!)
            clickSignUp()
            isValidationErrorDialogShown()
        }
    }

    @Test
    fun signWithSuccess() {
        signUp {
            setUserName(TestDataFactory.getNewUser().userName!!)
            setPassword(TestDataFactory.getNewUser().passWord!!)
            clickSignUp()
        }
    }

    @Test
    fun login() {
        signUp {
            clickLogin()
        }
        login {
            isLoginScreen()
        }
    }

}