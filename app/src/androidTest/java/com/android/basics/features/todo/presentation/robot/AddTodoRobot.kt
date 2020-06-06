package com.android.basics.features.todo.presentation.robot

import com.android.basics.R
import com.android.basics.utils.ScreenRobot

fun addTodo(func: AddTodoRobot.() -> Unit) = AddTodoRobot().apply { func() }

class AddTodoRobot : ScreenRobot() {

    fun isAddTodoScreen() = checkIsDisplayed(R.id.addtodo_root)

    fun isNameLabelShown() =
        checkTextInputLayoutHasText(R.id.txt_todo_add_name, R.string.name_label)

    fun isNameInputShown() = checkIsDisplayed(R.id.edt_todo_add_name)
    fun isDescriptionLabelShown() =
        checkTextInputLayoutHasText(R.id.txt_todo_add_description, R.string.description_label)

    fun isDescriptionInputShown() = checkIsDisplayed(R.id.edt_todo_add_description)
    fun isDateLabelShown() =
        checkTextInputLayoutHasText(R.id.txt_todo_add_date, R.string.date_label)

    fun isDateInputShown() = checkIsDisplayed(R.id.edt_todo_add_date)
    fun isSubmitShown() = checkIsDisplayed(R.id.btn_todo_todo_submit)
    fun setName(name: String) =
        enterTextIntoViewAndCloseKeyBoard(R.id.edt_todo_add_name, name)

    fun setDescription(description: String) =
        enterTextIntoViewAndCloseKeyBoard(R.id.edt_todo_add_description, description)
    fun setDate(date: String) =
        replaceTextIntoView(R.id.edt_todo_add_date, date)

    fun clickSubmit() = clickOnView(R.id.btn_todo_todo_submit)
    fun isValidationErrorDialogShown() =
        checkDialogWithTextIsDisplayed(R.string.addtodo_validation_error)

    fun isSuccessDialogShown() =
        checkDialogWithTextIsDisplayed(R.string.todo_add_success_msg)

    //fun clickDateInput() = clickOnView(R.id.edt_todo_add_date)

}