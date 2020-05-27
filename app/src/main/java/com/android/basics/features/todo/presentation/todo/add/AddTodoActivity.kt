package com.android.basics.features.todo.presentation.todo.add

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.ProgressDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.ImageButton
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.android.basics.R
import com.android.basics.core.di.ServiceLocator
import com.android.basics.core.exception.Failure
import com.android.basics.core.extension.getViewModelFactory
import com.android.basics.core.functional.ResourceStatus
import java.util.*


class AddTodoActivity : AppCompatActivity() {

    private val viewModel by viewModels<AddTodoViewModel> { getViewModelFactory() }
    private lateinit var edtName: EditText
    private lateinit var edtDescription: EditText
    private lateinit var edtDate: EditText
    private lateinit var btnSubmit: Button
    private lateinit var btnCancel: Button
    private lateinit var btnDate: ImageButton
    private lateinit var progressDialog: ProgressDialog
    private lateinit var builder: AlertDialog.Builder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_todo)
        initViews()
    }

    private fun initViews() {
        title = "Add Todo"

        builder = AlertDialog.Builder(this)

        edtName = findViewById(R.id.edt_todo_add_name)
        edtDescription = findViewById(R.id.edt_todo_add_description)
        edtDate = findViewById(R.id.edt_todo_add_date)

        btnSubmit = findViewById(R.id.btn_todo_todo_submit)
        btnCancel = findViewById(R.id.btn_todo_add_cancel)
        btnDate = findViewById(R.id.btn_todo_add_date)
        intProgressDialog()
        btnSubmit.setOnClickListener {
            viewModel.onSubmit(
                edtName.text.toString(),
                edtDescription.text.toString(),
                edtDate.text.toString()
            )
        }
        btnCancel.setOnClickListener { viewModel.onCancel() }
        btnDate.setOnClickListener { viewModel.onSelectDate() }
    }

    private fun intProgressDialog() {
        progressDialog = ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Logging in");
    }

    private fun showProgressDialog() {
        progressDialog.setMessage("Submitting ...")
        progressDialog.show()
    }


    private fun dismissProgressDialog() {
        progressDialog.dismiss()
        progressDialog.cancel()
    }


    private fun showSuccessDialog() {

        //Setting message manually and performing action on button click
        builder.setMessage("New record successfully inserted.")
            .setCancelable(false)
            .setPositiveButton(
                "Ok"
            ) { dialog: DialogInterface, id: Int ->
                dialog.dismiss()
                viewModel.navigate()
            }
        //Creating dialog box
        val alert = builder.create()
        alert.show()
    }


    private fun showErrorDialog(errorMsg: String = "There was a problem. could not able to insert the record.") {

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


    private fun showDatePickerDialog() {
        val mYear: Int
        val mMonth: Int
        val mDay: Int
        var mHour: Int
        var mMinute: Int
        // Get Current Date
        val c: Calendar = Calendar.getInstance()
        mYear = c.get(Calendar.YEAR)
        mMonth = c.get(Calendar.MONTH)
        mDay = c.get(Calendar.DAY_OF_MONTH)
        val datePickerDialog = DatePickerDialog(
            this,
            OnDateSetListener { view: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                edtDate.setText("$dayOfMonth/${monthOfYear + 1}/$year")
            },
            mYear, mMonth, mDay
        )
        datePickerDialog.show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.form_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.getItemId()) {
            R.id.menu_action_cancel -> {
                viewModel.onCancel()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onPause() {
        super.onPause()
        dismissProgressDialog()
        ServiceLocator.provideNavigator().onViewDestroyed()
    }

    override fun onStart() {
        super.onStart()
        ServiceLocator.provideNavigator().attachView(this, lifecycle)
        viewModel.state.observe(this,
            Observer {
                when (it.status) {
                    ResourceStatus.SUCCESS -> {
                        dismissProgressDialog();
                        showSuccessDialog();
                    }
                    ResourceStatus.ERROR -> {
                        dismissProgressDialog()
                        when (it.failure) {
                            is Failure.ValidationDataError -> showErrorDialog(it.failure.error);
                            else -> showErrorDialog()
                        }
                    }
                    ResourceStatus.LOADING -> showProgressDialog()
                }
            }
        )
        viewModel.showDatePickerEvent.observe(this, Observer { showDatePickerDialog() })
    }
}
