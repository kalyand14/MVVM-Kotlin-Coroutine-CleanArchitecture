package com.android.basics.features.todo.presentation.splash

import android.os.Bundle
import android.os.Handler
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.android.basics.R
import com.android.basics.core.extension.getViewModelFactory
import com.android.basics.core.navigation.Navigator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    @Inject
    lateinit var navigator: Navigator

    private val viewModel: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        Handler().postDelayed({ viewModel.navigate() }, 1000)
    }

    override fun onStart() {
        super.onStart()
        navigator.attachView(this, lifecycle)
    }

    override fun onPause() {
        super.onPause()
        navigator.onViewDestroyed()
    }
}
