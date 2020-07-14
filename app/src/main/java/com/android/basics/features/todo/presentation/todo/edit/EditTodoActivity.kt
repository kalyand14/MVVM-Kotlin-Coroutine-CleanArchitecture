package com.android.basics.features.todo.presentation.todo.edit

import android.app.ProgressDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.android.basics.R
import com.android.basics.core.di.ServiceLocator
import com.android.basics.core.exception.Failure
import com.android.basics.core.extension.getViewModelFactory
import com.android.basics.core.functional.ResourceStatus
import com.android.basics.core.navigation.Navigator
import com.android.basics.features.todo.presentation.todo.add.AddTodoViewModel
import com.google.android.material.button.MaterialButton
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class EditTodoActivity : AppCompatActivity() {
    @Inject
    lateinit var navigator: Navigator

    private val viewModel: EditTodoViewModel by viewModels()

    private lateinit var progressDialog: ProgressDialog
    private lateinit var builder: AlertDialog.Builder
    private lateinit var edtName: TextInputEditText
    private lateinit var edtDescription: TextInputEditText
    private lateinit var edtDate: TextInputEditText
    private lateinit var btnSubmit: MaterialButton
    private lateinit var btnDelete: MaterialButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_todo)
        initViews()
    }

    private fun initViews() {
        title = "Edit Todo"
        builder = AlertDialog.Builder(this)
        edtName = findViewById(R.id.edt_todo_edit_name)
        edtDescription = findViewById(R.id.edt_todo_edit_description)
        edtDate = findViewById(R.id.edt_todo_add_date)
        btnSubmit = findViewById(R.id.btn_todo_update)
        btnDelete = findViewById(R.id.btn_todo_delete)
        intProgressDialog()
        btnSubmit.setOnClickListener {
            viewModel.onSubmit(
                edtName.text.toString(),
                edtDescription.text.toString(),
                edtDate.text.toString()
            )
        }
        btnDelete.setOnClickListener { viewModel.onDelete() }
        edtDate.setOnClickListener { viewModel.onSelectDate() }

    }

    private fun intProgressDialog() {
        progressDialog = ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Logging in");
    }

    private fun showProgressDialog(message: String?) {
        progressDialog.setMessage(message)
        progressDialog.show()
    }


    private fun dismissProgressDialog() {
        progressDialog.dismiss()
        progressDialog.cancel()
    }


    private fun showSuccessDialog(message: String?) {

        //Setting message manually and performing action on button click
        builder.setMessage(message)
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


    private fun showErrorDialog(message: String?) {
        //Setting message manually and performing action on button click
        builder.setMessage(message)
            .setCancelable(false)
            .setPositiveButton(
                "Ok"
            ) { dialog: DialogInterface, id: Int -> dialog.dismiss() }
        //Creating dialog box
        val alert = builder.create()
        alert.setTitle("Error")
        alert.show()
    }

    private val outputDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }

    private fun showDatePickerDialog() {
        val builder: MaterialDatePicker.Builder<Long> =
            MaterialDatePicker.Builder.datePicker()
        builder.setSelection(MaterialDatePicker.todayInUtcMilliseconds())
        val picker: MaterialDatePicker<Long> = builder.build()
        picker.addOnPositiveButtonClickListener {
            edtDate.setText(outputDateFormat.format(it))
        }
        picker.show(supportFragmentManager, picker.toString())
    }


    private fun setName(name: String?) {
        edtName.setText(name)
    }


    private fun setDescription(description: String?) {
        edtDescription.setText(description)
    }


    private fun setDate(date: String?) {
        edtDate.setText(date)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
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
        navigator.onViewDestroyed()
    }

    override fun onStart() {
        super.onStart()
        navigator.attachView(this, lifecycle)
        viewModel.loadTodoEvent.observe(this, Observer {
            it?.run {
                setName(name)
                setDescription(description)
                setDate(dueDate)
            }
        })
        viewModel.editTodoState.observe(this,
            Observer {
                when (it.status) {
                    ResourceStatus.SUCCESS -> {
                        dismissProgressDialog()
                        showSuccessDialog("Record successfully updated.")
                    }
                    ResourceStatus.ERROR -> {
                        dismissProgressDialog()
                        when (it.failure) {
                            is Failure.ValidationDataError -> showErrorDialog(it.failure.error);
                            else -> showErrorDialog("There was a problem. could not able to update the record.");
                        }
                    }
                    ResourceStatus.LOADING -> showProgressDialog("Updating todo");
                }
            }
        )

        viewModel.deleteTodoState.observe(this,
            Observer {
                when (it.status) {
                    ResourceStatus.SUCCESS -> {
                        dismissProgressDialog()
                        showSuccessDialog("Record successfully deleted.");
                    }
                    ResourceStatus.ERROR -> {
                        dismissProgressDialog()
                        showErrorDialog("Error deleting todo");
                    }
                    ResourceStatus.LOADING -> showProgressDialog("Deleting todo");
                }
            }
        )
        viewModel.showDatePickerEvent.observe(this, Observer { showDatePickerDialog() })

        viewModel.loadTodo();
    }
}
