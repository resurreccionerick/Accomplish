package com.example.todolist

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.update_todo_dialog.view.*


class ToDoAdapter(private val toDoList: ArrayList<ToDoModel>, private val context: Context) :
    RecyclerView.Adapter<ToDoAdapter.ToDoViewHolder>() {

    private lateinit var databaseReference: DatabaseReference
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_todo, parent, false)
        return ToDoViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ToDoViewHolder, position: Int) {
        val curToDo = toDoList[position]

        databaseReference = FirebaseDatabase.getInstance().getReference("Users")
        firebaseAuth = Firebase.auth

        val key = curToDo.id //unique id
        val name = firebaseAuth.currentUser?.displayName
        val checked = curToDo.checked

        holder.title.text = curToDo.title

        if (curToDo.checked == "true") {
            holder.checkBox.isChecked = true
        }

        holder.checkBox.setOnClickListener(View.OnClickListener { view ->
            databaseReference =
                FirebaseDatabase.getInstance().getReference("Users").child(name.toString())
                    .child(key.toString())

            if ((view as CompoundButton).isChecked) {
                val updateValues = mapOf(
                    "checked" to "true"
                )

                databaseReference.updateChildren(updateValues)

            } else {
                val updateValues = mapOf(
                    "checked" to "false"
                )

                databaseReference.updateChildren(updateValues)
            }

            context.startActivity(
                Intent(
                    context,
                    MainActivity::class.java
                )
            ) //restart the activity
        })


        holder.todoLayout.setOnLongClickListener {
            holder.updateDeleteLayout.isVisible = true
            return@setOnLongClickListener true
        }

        holder.btnUpdate.setOnClickListener {
            //btn click to show dialog
            val updateDialog =
                LayoutInflater.from(context).inflate(R.layout.update_todo_dialog, null)

            //alert dialog builder
            val mBuilder = AlertDialog.Builder(context).setView(updateDialog).setTitle("Edit")

            //show dialog
            val mAlertDialog = mBuilder.show()

            updateDialog.updateTxtToDoTitle.setText(curToDo.title)

            updateDialog.btnUpdate.setOnClickListener {
                mAlertDialog.dismiss()

                val updateTxtToDoTitle: EditText =
                    updateDialog.findViewById(R.id.updateTxtToDoTitle)

                val updateValues = mapOf(
                    "id" to key,
                    "title" to updateTxtToDoTitle.text.toString(),
                    "checked" to checked
                )

                databaseReference.child(name.toString()).child(key.toString())
                    .setValue(updateValues)
                    .addOnSuccessListener {
                        updateTxtToDoTitle.text.clear()
                        Toast.makeText(
                            context, "Successfully Updated",
                            Toast.LENGTH_SHORT
                        ).show()

                        context.startActivity(
                            Intent(
                                context,
                                MainActivity::class.java
                            )
                        ) //restart the activity
                    }
            }
        }

        holder.btnDelete.setOnClickListener {
            val builder = AlertDialog.Builder(context)
            builder.setMessage("Are you sure you want to Delete?")
                .setCancelable(false)
                .setPositiveButton("Yes") { dialog, id ->
                    databaseReference.child(firebaseAuth.currentUser?.displayName.toString())
                        .child(curToDo.title.toString())
                    databaseReference.removeValue()

                    context.startActivity(
                        Intent(
                            context,
                            MainActivity::class.java
                        )
                    ) //restart the activity
                }
                .setNegativeButton("No") { dialog, id ->
                    // Dismiss the dialog
                    dialog.dismiss()
                }
            val alert = builder.create()
            alert.show()
        }

        holder.btnCancel.setOnClickListener {
            holder.updateDeleteLayout.isVisible = false
        }

    }

    override fun getItemCount(): Int {
        return toDoList.size
    }

    class ToDoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.tvToDoTitle)
        val checkBox: CheckBox = itemView.findViewById(R.id.cbDone)
        val updateDeleteLayout: LinearLayout = itemView.findViewById(R.id.updateDeleteLayout)
        val btnDelete: ImageView = itemView.findViewById(R.id.btnDelete)
        val btnUpdate: ImageView = itemView.findViewById(R.id.btnEdit)
        val btnCancel: TextView = itemView.findViewById(R.id.btnCancel)
        val todoLayout: LinearLayout = itemView.findViewById(R.id.todoLayout)
    }
}