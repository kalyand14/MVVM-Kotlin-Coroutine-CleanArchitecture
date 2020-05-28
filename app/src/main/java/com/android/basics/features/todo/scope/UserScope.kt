package com.android.basics.features.todo.scope

import com.android.basics.core.scope.BaseScope
import com.android.basics.features.todo.domain.model.Todo
import com.android.basics.features.todo.domain.model.User


object UserScope : BaseScope {
    var user: User? = null
    var todoList: List<Todo>? = null

    override fun end() {
        TodoScope.end()
        user = null
        todoList = null
    }

}