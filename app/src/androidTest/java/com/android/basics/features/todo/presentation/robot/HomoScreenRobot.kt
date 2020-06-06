package com.android.basics.features.todo.presentation.robot

import com.android.basics.R
import com.android.basics.utils.ScreenRobot

fun home(func: HomoScreenRobot.() -> Unit) = HomoScreenRobot().apply { func() }

class HomoScreenRobot : ScreenRobot() {
    fun isHomeScreen() = checkIsDisplayed(R.id.home_root)
    fun isErrorShown() = checkViewHasText(R.id.tv_message, R.string.home_no_todo_error)
    fun isItemShownAt(position: Int, text: String) =
        checkRecyclerViewItem(R.id.recyclerView, position, text)
}