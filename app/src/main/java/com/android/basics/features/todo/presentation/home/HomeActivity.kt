package com.android.basics.features.todo.presentation.home

import android.app.ProgressDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.basics.R
import com.android.basics.core.di.ServiceLocator
import com.android.basics.core.extension.getViewModelFactory
import com.android.basics.core.functional.ResourceStatus
import com.android.basics.core.navigation.Navigator
import com.android.basics.features.todo.domain.model.Todo
import com.android.basics.features.todo.presentation.home.components.TodoListAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton


class HomeActivity : AppCompatActivity() {

    private val viewModel by viewModels<HomeScreenViewModel> { getViewModelFactory() }

    private lateinit var progressDialog: ProgressDialog
    private lateinit var recyclerView: RecyclerView
    private lateinit var todoListAdapter: TodoListAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var txtError: TextView
    private lateinit var floatingActionButton: FloatingActionButton
    private lateinit var builder: AlertDialog.Builder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        builder = AlertDialog.Builder(this)

        floatingActionButton = findViewById(R.id.fab)
        txtError = findViewById(R.id.tv_message)
        recyclerView = findViewById(R.id.recyclerView)
        layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = RecyclerView.VERTICAL
        recyclerView.layoutManager = layoutManager
        recyclerView.itemAnimator = DefaultItemAnimator()

        injectView(this)
        injectObject(this)

        recyclerView.adapter = todoListAdapter

        viewModel.onLoadTodoList("1")

        floatingActionButton.setOnClickListener { view: View? -> { { TODO() } } }
    }

    private fun injectView(activity: HomeActivity) {
        activity.progressDialog = ProgressDialog(activity)
        activity.progressDialog.isIndeterminate = true
        activity.progressDialog.setMessage("Logging in")
    }

    private fun injectObject(activity: HomeActivity) {
        ServiceLocator.provideNavigator().attachView(this, lifecycle)
        activity.todoListAdapter = ServiceLocator.provideTodoListAdapter()
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


    override fun onPause() {
        super.onPause()
        dismissProgressDialog()
    }


    fun showProgressDialog() {
        progressDialog.setMessage("Loading todo list")
        progressDialog.show()
    }


    fun dismissProgressDialog() {
        progressDialog.dismiss()
        progressDialog.cancel()
    }


    fun showErrorLayout(display: Boolean) {
        txtError.visibility = if (display) View.VISIBLE else View.GONE
    }


    fun showList(display: Boolean) {
        recyclerView.visibility = if (display) View.VISIBLE else View.GONE
    }


    fun loadTodoList(todoList: List<Todo>?) {
        todoListAdapter.addItems(todoList)
    }


    fun setWelcomeMessage(message: String?) {
        title = message
    }

    fun showLogoutConfirmationDialog() {
        //Setting message manually and performing action on button click
        builder.setMessage("Do you want to log out?")
            .setCancelable(false)
            .setPositiveButton(
                "Yes"
            ) { dialog: DialogInterface, id: Int ->
                dialog.dismiss()
                //viewModel.logout()
            }
            .setNegativeButton(
                "No"
            ) { dialog: DialogInterface, id: Int -> dialog.dismiss() }
        //Creating dialog box
        val alert = builder.create()
        alert.setTitle("Logout")
        alert.show()
    }

}
