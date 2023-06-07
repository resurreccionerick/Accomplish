package com.example.accomplish_to_do_app.utils

import android.app.AlertDialog
import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.example.accomplish_to_do_app.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class ToDoAdapter(
    private val taskList: ArrayList<ToDoModel>,
    private val navController: NavController
) : RecyclerView.Adapter<ToDoAdapter.TaskViewHolder>() {


    private lateinit var auth: FirebaseAuth
    private lateinit var databaseRef: DatabaseReference


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.todo_item, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val taskTodo = taskList[position]

        auth = FirebaseAuth.getInstance()
        databaseRef = FirebaseDatabase.getInstance().reference
            .child("Tasks").child(auth.currentUser?.uid.toString())

        holder.taskTextView.text = taskTodo.todo

        holder.taskCheckBox.isChecked = taskTodo.checked == true


        holder.taskCheckBox.setOnCheckedChangeListener { _, isChecked ->
            databaseRef.child(taskTodo.id.toString()).child("checked").setValue(isChecked).addOnCompleteListener {
                if (it.isSuccessful) {
                    taskList[position].checked = isChecked

                    navController.navigate(R.id.action_taskFragment_self) //restart fragment
                }
            }
        }

        holder.btnEdit.setOnClickListener {
            val dialogView = LayoutInflater.from(holder.itemView.context).inflate(R.layout.todo_popup, null)

            val dialog = AlertDialog.Builder(holder.itemView.context)
                .setView(dialogView)
                .create()

            val txtDialogEditTodo = dialogView.findViewById<EditText>(R.id.txtDialogAddTodo)
            val btnDialogUpdateTodo = dialogView.findViewById<Button>(R.id.btnDialogAddNewTodo)

            txtDialogEditTodo.setText(taskTodo.todo)

            btnDialogUpdateTodo.setOnClickListener {
                val updatedTodo = txtDialogEditTodo.text.toString()

                databaseRef.child(taskTodo.id.toString()).child("todo").setValue(updatedTodo)

                navController.navigate(R.id.action_taskFragment_self) //restart fragment
                dialog.dismiss()
            }

            dialog.show()
        }

        holder.btnDelete.setOnClickListener {
            val builder = AlertDialog.Builder(holder.itemView.context)
            builder.setTitle("Confirmation")
                .setMessage("Are you sure you want to delete?")
                .setPositiveButton("Delete") { dialog, _ ->
                    databaseRef.child(taskTodo.id.toString()).removeValue()

                    navController.navigate(R.id.action_taskFragment_self) //restart fragment
                    dialog.dismiss()
                }
                .setNegativeButton("Cancel") { dialog, _ ->
                    dialog.dismiss()
                }

            val alertDialog = builder.create()
            alertDialog.show()
        }
    }

    override fun getItemCount(): Int {
        return taskList.size
    }

    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var taskTextView: TextView = itemView.findViewById(R.id.txtToDoItem)
        var taskCheckBox: CheckBox = itemView.findViewById(R.id.cBoxToDoItem)
        var btnEdit : ImageView = itemView.findViewById(R.id.btnEditToDoItem)
        var btnDelete : ImageView = itemView.findViewById(R.id.btnDeleteToDoItem)

    }
}
