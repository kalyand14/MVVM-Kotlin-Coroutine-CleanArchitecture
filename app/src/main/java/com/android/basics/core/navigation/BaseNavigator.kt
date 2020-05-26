package com.android.basics.core.navigation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity


interface BaseNavigator {
    fun launchActivity(intent: Intent?)
    fun finishActivity()
    fun closeApplication()
    fun createIntent(clazz: Class<out AppCompatActivity?>?): Intent?
}