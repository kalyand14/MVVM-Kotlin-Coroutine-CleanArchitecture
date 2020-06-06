package com.android.basics.features.todo.presentation.robot

import com.android.basics.R
import com.android.basics.utils.ScreenRobot

fun signUp(func: SignUpRobot.() -> Unit) = SignUpRobot().apply { func() }

class SignUpRobot : ScreenRobot() {

    fun setUserName(userName: String) = enterTextIntoView(R.id.edt_signup_username, userName)

    fun setPassword(password: String) =
        enterTextIntoViewAndCloseKeyBoard(R.id.edt_signup_password, password)

    fun clickLogin() = clickOnView(R.id.btn_signup);

    fun isValidationErrorDialogShown() =
        checkDialogWithTextIsDisplayed(R.string.login_validation_error)

    fun isCredentialErrorDialogShown() =
        checkDialogWithTextIsDisplayed(R.string.login_credential_error)


}