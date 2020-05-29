package com.android.basics.features.todo.presentation.robot

import com.android.basics.R
import com.android.basics.utils.ScreenRobot


fun login(func: LoginRobot.() -> Unit) = LoginRobot().apply { func() }

class LoginRobot : ScreenRobot() {

    fun setUserName(userName: String) = enterTextIntoViewAndCloseKeyBoard(R.id.edt_login_username, userName)

    fun setPassword(password: String) =
        enterTextIntoViewAndCloseKeyBoard(R.id.edt_login_password, password)

    fun clickLogin() = clickOnView(R.id.btn_login);

    fun isValidationErrorDialogShown() =
        checkDialogWithTextIsDisplayed(R.string.login_validation_error)

    fun isCredentialErrorDialogShown() =
        checkDialogWithTextIsDisplayed(R.string.login_credential_error)


}
