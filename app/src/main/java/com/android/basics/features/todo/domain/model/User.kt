package com.android.basics.features.todo.domain.model

data class User(var userId: String? = null, var userName: String?, var passWord: String?) {
    private val isUserNameValid = userName?.let {
        it.isNotBlank() && it.isNotEmpty()
    } ?: false

    private val isPasswordValid = passWord?.let {
        it.isNotBlank() && it.isNotEmpty()
    } ?: false

    var isValid = isUserNameValid && isPasswordValid
        private set


}