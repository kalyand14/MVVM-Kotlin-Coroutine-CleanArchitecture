package com.android.basics.core.navigation

import android.content.Context
import android.content.Intent

interface IntentFactory {
    fun create(context: Context?, clazz: Class<out Context?>?): Intent?

    fun create(action: String?): Intent?

    fun create(): Intent?
}