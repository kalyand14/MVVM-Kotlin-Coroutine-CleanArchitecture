package com.android.basics.core.extension

import android.app.Activity
import com.android.basics.TodoApplication
import com.android.basics.features.todo.presentation.di.ViewModelFactory

fun Activity.getViewModelFactory(): ViewModelFactory {
    return ViewModelFactory((applicationContext as TodoApplication))
}