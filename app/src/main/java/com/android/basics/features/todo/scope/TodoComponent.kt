package com.android.basics.features.todo.scope

import com.android.basics.core.scope.BaseScope
import com.android.basics.core.scope.InstanceContainer


class TodoComponent private constructor() : BaseScope {
    val container = InstanceContainer()

    override fun end() {
        container.end()
    }

    companion object {
        val instance: TodoComponent
            get() {
                if (!UserComponent.instance.container.has(TodoComponent::class.java)) {
                    UserComponent.instance.container[TodoComponent::class.java] = TodoComponent()
                }
                return UserComponent.instance.container[TodoComponent::class.java] as TodoComponent
            }
    }
}