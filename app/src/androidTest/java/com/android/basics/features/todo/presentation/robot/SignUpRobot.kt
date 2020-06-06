package com.android.basics.features.todo.presentation.robot

import com.android.basics.R
import com.android.basics.utils.ScreenRobot

fun signUp(func: SignUpRobot.() -> Unit) = SignUpRobot().apply { func() }

class SignUpRobot : ScreenRobot() {

    fun isSignUpScreen() = checkIsDisplayed(R.id.signup_root)

    fun isHeaderShown() = checkViewHasText(R.id.txt_login_msg, R.string.sign_up)

    fun isUserNameLabelShown() = checkViewHasText(R.id.txt_signup_username, R.string.username)

    fun isPasswordLabelShown() = checkViewHasText(R.id.txt_signup_password, R.string.password)

    fun isLoginButtonShown() = checkViewHasText(R.id.btn_login, R.string.log_in)

    fun isSignUpButtonShown() = checkViewHasText(R.id.btn_signup, R.string.sign_up)

    fun isSignUpButtonLabelShown() =
        checkViewHasText(R.id.txt_signup, R.string.already_have_an_account)

    fun setUserName(userName: String) =
        enterTextIntoViewAndCloseKeyBoard(R.id.edt_signup_username, userName)

    fun setPassword(password: String) =
        enterTextIntoViewAndCloseKeyBoard(R.id.edt_signup_password, password)

    fun clickSignUp() = clickOnView(R.id.btn_signup);

    fun clickLogin() = clickOnView(R.id.btn_login);

    fun isValidationErrorDialogShown() =
        checkDialogWithTextIsDisplayed(R.string.login_validation_error)

    fun isCredentialErrorDialogShown() =
        checkDialogWithTextIsDisplayed(R.string.login_credential_error)


}