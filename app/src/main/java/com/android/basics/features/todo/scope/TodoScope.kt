package com.android.basics.features.todo.scope

import com.android.basics.core.scope.BaseScope
import com.android.basics.features.todo.domain.model.Todo


object TodoScope : BaseScope {
    var todo: Todo? = null
    override fun end() {
        todo = null
    }

}