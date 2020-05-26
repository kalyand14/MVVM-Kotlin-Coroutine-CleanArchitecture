package com.android.basics.features.todo.presentation.components

import com.android.basics.core.scope.ScopeObserver
import com.android.basics.features.todo.domain.model.Todo
import com.android.basics.features.todo.scope.TodoComponent


class TodoSession private constructor() : ScopeObserver {
    var todo: Todo? = null

    override fun onScopeEnded() {
        todo = null
    }

    companion object {
        val instance: TodoSession
            get() {
                if (!TodoComponent.instance.container.has(TodoSession::class.java)) {
                    TodoComponent.instance.container[TodoSession::class.java] = TodoSession()
                }
                return TodoComponent.instance.container[TodoSession::class.java] as TodoSession
            }
    }
}