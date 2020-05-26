package com.android.basics.features.todo.scope

import com.android.basics.core.di.ServiceLocator
import com.android.basics.core.scope.BaseScope
import com.android.basics.core.scope.InstanceContainer


class UserComponent private constructor() : BaseScope {

    val container = InstanceContainer()

    override fun end() {
        container.end()
    }

    companion object {
        val instance: UserComponent
            get() {
                if (!ServiceLocator.getContainer().has(UserComponent::class.java)) {
                    ServiceLocator.getContainer()[UserComponent::class.java] = UserComponent()
                }
                return ServiceLocator.getContainer()[UserComponent::class.java] as UserComponent
            }
    }
}