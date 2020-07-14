package com.android.basics.features.todo.presentation.home

import android.app.ProgressDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
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
import com.android.basics.features.todo.presentation.login.LoginViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    @Inject
    lateinit var navigator: Navigator

    @Inject
    lateinit var todoListAdapter: TodoListAdapter

    private val viewModel: HomeScreenViewModel by viewModels()

    private lateinit var progressDialog: ProgressDialog
    private lateinit var recyclerView: RecyclerView
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

        recyclerView.adapter = todoListAdapter

        floatingActionButton.setOnClickListener { viewModel.onAddTodo() }
    }

    private fun injectView(activity: HomeActivity) {
        activity.progressDialog = ProgressDialog(activity)
        activity.progressDialog.isIndeterminate = true
        activity.progressDialog.setMessage("Logging in")
    }


    override fun onStart() {
        super.onStart()
        navigator.attachView(this, lifecycle)

        viewModel.welcomeMessageEvent.observe(this, Observer { setWelcomeMessage(it) });
        viewModel.state.observe(this,
            Observer {
                when (it.status) {
                    ResourceStatus.SUCCESS -> {
                        Timber.i("Loading Todo List. This list contains ${it.data?.size} rows");
                        if (it.data != null && it.data.isNotEmpty()) {
                            dismissProgressDialog()
                            showList(true)
                            showErrorLayout(false)
                            loadTodoList(it.data)
                        } else {
                            showError()
                        }
                    }
                    ResourceStatus.ERROR -> {
                        showError()
                    }
                    ResourceStatus.LOADING -> showProgressDialog()
                }
            }
        )
        viewModel.loggedOutEvent.observe(this, Observer { showLogoutConfirmationDialog() })

        viewModel.onLoadTodoList()
    }

    private fun showError() {
        dismissProgressDialog()
        showList(false)
        showErrorLayout(true)
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
                viewModel.logout()
            }
            .setNegativeButton(
                "No"
            ) { dialog: DialogInterface, id: Int -> dialog.dismiss() }
        //Creating dialog box
        val alert = builder.create()
        alert.setTitle("Logout")
        alert.show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.getItemId()) {
            R.id.menu_action_logout -> {
                viewModel.onLogout()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
