package com.android.basics.features.todo.presentation.login

import android.app.ProgressDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.android.basics.R
import com.android.basics.core.di.ServiceLocator
import com.android.basics.core.exception.Failure
import com.android.basics.core.extension.getViewModelFactory
import com.android.basics.core.functional.ResourceStatus
import com.android.basics.features.todo.scope.UserScope


class LoginActivity : AppCompatActivity() {

    private val viewModel by viewModels<LoginViewModel> { getViewModelFactory() }

    private lateinit var progressDialog: ProgressDialog
    private lateinit var btnLogin: Button
    private lateinit var btnRegister: Button
    private lateinit var edtUserName: EditText
    private lateinit var edtPassword: EditText
    private lateinit var builder: AlertDialog.Builder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initViews()
    }

   private fun initViews() {
        btnLogin = findViewById(R.id.btn_add_todo)
        btnRegister = findViewById(R.id.btn_signup)
        edtUserName = findViewById(R.id.edt_todo_name)
        edtPassword = findViewById(R.id.edt_todo_description)
        builder = AlertDialog.Builder(this)
        intProgressDialog()

        btnLogin.run {
            setOnClickListener(View.OnClickListener {
                viewModel.onLoginClick(
                    edtUserName.text.toString(),
                    edtPassword.text.toString()
                )
            })
        }
        btnRegister.run { setOnClickListener(View.OnClickListener { viewModel.onRegisterClick() }) }

        UserScope.end()
    }

    private fun intProgressDialog() {
        progressDialog = ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Logging in");
    }


    private fun showProgressDialog() {
        progressDialog.setMessage("Logging in")
        progressDialog.show()
    }

    private fun dismissProgressDialog() {
        progressDialog.dismiss()
        progressDialog.cancel()
    }

    private fun showAuthenticationError(errorMsg: String = "There was a problem logging in. Check your credentials or create an account.") {
        edtUserName.setText("")
        edtPassword.setText("")

        //Setting message manually and performing action on button click
        builder.setMessage(errorMsg)
            .setCancelable(false)
            .setPositiveButton(
                "Ok"
            ) { dialog: DialogInterface, id: Int -> dialog.dismiss() }
        //Creating dialog box
        val alert = builder.create()
        alert.setTitle("Error")
        alert.show()
    }

    override fun onStart() {
        super.onStart()

        ServiceLocator.provideNavigator().attachView(this, lifecycle)

        viewModel.state.observe(this,
            Observer {
                when (it.status) {
                    ResourceStatus.SUCCESS -> dismissProgressDialog()
                    ResourceStatus.ERROR -> {
                        dismissProgressDialog()
                        when (it.failure) {
                            is Failure.ValidationDataError -> showAuthenticationError(it.failure.error);
                            else -> showAuthenticationError()
                        }
                    }
                    ResourceStatus.LOADING -> showProgressDialog()
                }
            }
        )
    }

    override fun onPause() {
        super.onPause()
        dismissProgressDialog()
        ServiceLocator.provideNavigator().onViewDestroyed()
    }
}
