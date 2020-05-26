package com.android.basics.features.todo.presentation.home.components

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.android.basics.R
import com.android.basics.core.components.BaseViewHolder
import com.android.basics.features.todo.presentation.components.TodoCoordinator
import com.android.basics.features.todo.presentation.components.TodoSession
import com.android.basics.features.todo.domain.model.Todo
import com.android.basics.features.todo.scope.TodoComponent


class TodoListAdapter(
    private val todoList: MutableList<Todo>,
    private val coordinator: TodoCoordinator,
    private val todoSession: TodoSession,
    private val scope: TodoComponent

) :
    RecyclerView.Adapter<BaseViewHolder>() {

    @NonNull
    override fun onCreateViewHolder(
        @NonNull parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_todo_list, parent, false)
        )
    }

    override fun onBindViewHolder(
        @NonNull holder: BaseViewHolder,
        position: Int
    ) {
        holder.onBind(position)
    }

    override fun getItemCount(): Int {
        return todoList.size
    }

    fun addItems(todoList: List<Todo>?) {
        this.todoList.addAll(todoList!!)
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : BaseViewHolder(itemView) {
        var txtName: TextView
        var txtDate: TextView
        var txtDescription: TextView
        override fun onBind(position: Int) {
            super.onBind(position)
            val todo = todoList[position]
            txtName.text = todo.name
            txtDate.text = todo.dueDate
            txtDescription.text = todo.description
            itemView.setOnClickListener { view: View? ->
                scope.end()
                // TODO need to implement it
                //todoSession.setTodo(todo)
                coordinator.goToEditTodoScreen()
            }
        }

        override fun clear() {
            txtName.text = ""
            txtDate.text = ""
            txtDescription.text = ""
        }

        init {
            txtName = itemView.findViewById(R.id.txt_name)
            txtDate = itemView.findViewById(R.id.txt_date)
            txtDescription = itemView.findViewById(R.id.txt_description)
        }
    }


}