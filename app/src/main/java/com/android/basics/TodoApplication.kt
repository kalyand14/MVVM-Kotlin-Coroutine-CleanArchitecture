package com.android.basics

import android.app.Application
import com.android.basics.core.logging.LineNumberDebugTree
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class TodoApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) Timber.plant(LineNumberDebugTree())
    }
}