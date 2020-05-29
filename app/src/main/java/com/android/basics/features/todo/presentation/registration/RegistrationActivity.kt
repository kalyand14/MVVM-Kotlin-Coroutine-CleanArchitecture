package com.android.basics.features.todo.presentation.registration

import android.app.ProgressDialog
import android.content.DialogInterface
import android.os.Bundle
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
import timber.log.Timber


class RegistrationActivity : AppCompatActivity() {

    private val viewModel by viewModels<RegistrationViewModel> { getViewModelFactory() }
    private lateinit var progressDialog: ProgressDialog
    private lateinit var btnLogin: Button
    private lateinit var btnRegister: Button
    private lateinit var edtUserName: EditText
    private lateinit var edtPassword: EditText
    private lateinit var builder: AlertDialog.Builder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        initViews()

        Timber.tag("RegistrationActivity");
        Timber.d("Activity Created");
    }

    private fun initViews() {
        btnLogin = findViewById(R.id.btn_login)
        btnRegister = findViewById(R.id.btn_signup)
        edtUserName = findViewById(R.id.edt_login_username)
        edtPassword = findViewById(R.id.edt_login_password)
        builder = AlertDialog.Builder(this)
        intProgressDialog()
        btnRegister.setOnClickListener {
            viewModel.onRegisterClick(
                edtUserName.text.toString(),
                edtPassword.text.toString()
            )
        }
        btnLogin.setOnClickListener { viewModel.onLoginClick() }
    }

    private fun intProgressDialog() {
        progressDialog = ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Logging in");
    }

    private fun showProgressDialog() {
        progressDialog.setMessage("Registering ...")
        progressDialog.show()
    }


    private fun dismissProgressDialog() {
        progressDialog.dismiss()
        progressDialog.cancel()
    }

    private fun showRegistrationError(errorMsg: String = "There was a problem. could not able to register with details.") {
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

    private fun showRegistrationSuccess() {
        //Setting message manually and performing action on button click
        builder.setMessage("you've successfully registered.")
            .setCancelable(false)
            .setPositiveButton(
                "Ok"
            ) { dialog: DialogInterface, id: Int ->
                dialog.dismiss()
                viewModel.onRegistrationSuccess()
            }
        //Creating dialog box
        val alert = builder.create()
        alert.setTitle("Congrats")
        alert.show()
    }

    override fun onStart() {
        super.onStart()
        ServiceLocator.provideNavigator().attachView(this, lifecycle)
        viewModel.state.observe(this,
            Observer {
                when (it.status) {
                    ResourceStatus.SUCCESS -> {
                        dismissProgressDialog();
                        showRegistrationSuccess();
                    }
                    ResourceStatus.ERROR -> {
                        dismissProgressDialog()
                        when (it.failure) {
                            is Failure.ValidationDataError -> showRegistrationError(it.failure.error);
                            else -> showRegistrationError()
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
