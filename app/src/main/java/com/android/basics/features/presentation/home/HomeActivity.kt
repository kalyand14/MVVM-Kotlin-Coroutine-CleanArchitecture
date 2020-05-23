package com.android.basics.features.presentation.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.android.basics.R
import com.android.basics.core.functional.ResourceStatus
import com.android.basics.features.domain.model.Todo


class HomeActivity : AppCompatActivity() {

    lateinit var viewModel: HomeScreenViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        viewModel.onLoadTodoList("1")
    }

    override fun onStart() {
        super.onStart()

        viewModel.state.observe(this,
            Observer {
                when (it.status) {
                    ResourceStatus.SUCCESS -> {
                        dismissProgressDialog()
                        showList(true)
                        showErrorLayout(false)
                        loadTodoList(it.data)
                    }
                    ResourceStatus.ERROR -> {
                        dismissProgressDialog()
                        showList(false)
                        showErrorLayout(true)
                    }
                    ResourceStatus.LOADING -> showProgressDialog()
                }
            }
        )
    }


    fun showProgressDialog() {

    }


    fun dismissProgressDialog() {

    }


    fun showErrorLayout(display: Boolean) {

    }


    fun showList(display: Boolean) {

    }


    fun loadTodoList(todoList: List<Todo?>?) {

    }
}
