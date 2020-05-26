package com.android.basics.features.todo.presentation.splash

import android.os.Bundle
import android.os.Handler
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.android.basics.R
import com.android.basics.core.di.ServiceLocator
import com.android.basics.core.extension.getViewModelFactory

class SplashActivity : AppCompatActivity() {

    private val viewModel by viewModels<SplashViewModel> { getViewModelFactory() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        inject()
        Handler().postDelayed({ viewModel.navigate() }, 1000)
    }

    private fun inject() {
        ServiceLocator.provideNavigator().attachView(this, lifecycle)
    }
}
