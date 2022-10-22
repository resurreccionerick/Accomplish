package com.example.todolist

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase


class AddNewToDoDialog(context: Context) : Dialog(context) {
    init {
        setCancelable(true)
    }

    private lateinit var databaseReference: DatabaseReference
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.add_todo_dialog)


        val txtToDoTitle: EditText = findViewById(R.id.txtToDoTitle)
        val btnAdd: Button = findViewById(R.id.btnAdd)


        btnAdd.setOnClickListener {
            if (txtToDoTitle.text.isEmpty()) {
                Toast.makeText(
                    context, "Please enter a title.",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                databaseReference = FirebaseDatabase.getInstance().getReference("Users")
                firebaseAuth = Firebase.auth
                val uniqueKey = databaseReference.push().key //unique id
                val name = firebaseAuth.currentUser?.displayName

                val toDo = ToDoModel(uniqueKey, txtToDoTitle.text.toString(), "false")

                txtToDoTitle.text.clear()

                databaseReference.child(name.toString()).child(uniqueKey.toString()).setValue(toDo)
                    .addOnSuccessListener {
                        Toast.makeText(
                            context, "Successfully Added",
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
    }
}