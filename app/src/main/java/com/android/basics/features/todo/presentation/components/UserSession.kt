package com.android.basics.features.todo.presentation.components

import com.android.basics.core.scope.ScopeObserver
import com.android.basics.features.todo.domain.model.Todo
import com.android.basics.features.todo.domain.model.User
import com.android.basics.features.todo.scope.UserComponent


class UserSession private constructor() : ScopeObserver {
    var user: User? = null
    var todoList: List<Todo>? = null

    override fun onScopeEnded() {
        user = null
    }

    companion object {
        val instance: UserSession
            get() {
                if (!UserComponent.instance.container.has(UserSession::class.java)) {
                    UserComponent.instance.container[UserSession::class.java] = UserSession()
                }
                return UserComponent.instance.container[UserSession::class.java] as UserSession
            }
    }
}