package com.android.basics.core.navigation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import java.lang.ref.WeakReference


class Navigator(private val intentFactory: IntentFactory) : BaseNavigator, LifecycleObserver {

    private var contextWeakRef: WeakReference<AppCompatActivity?>? = null
    private var viewLifecycle: Lifecycle? = null

    fun attachView(activity: AppCompatActivity?, viewLifecycle: Lifecycle) {
        contextWeakRef = WeakReference(activity)
        this.viewLifecycle = viewLifecycle;
        viewLifecycle.addObserver(this)
    }

    fun onViewDestroyed() {
        contextWeakRef?.clear()
        contextWeakRef = null
        viewLifecycle = null
    }

    override fun launchActivity(intent: Intent?) {
        contextWeakRef?.get()?.startActivity(intent)
    }

    override fun finishActivity() {
        contextWeakRef?.get()?.finish()
    }

    override fun closeApplication() {
        //TODO: need to change it
        contextWeakRef?.get()?.finishAffinity()
    }

    override fun createIntent(clazz: Class<out AppCompatActivity?>?): Intent? {
        return intentFactory.create(contextWeakRef?.get(), clazz)
    }
}