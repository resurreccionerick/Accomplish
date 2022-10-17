package com.example.todolist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.add_todo_dialog.*

class MainActivity : AppCompatActivity() {

    private lateinit var databaseReference: DatabaseReference
    private lateinit var rvToDoItems: RecyclerView
    private lateinit var toDoArrayList: ArrayList<ToDoModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fbAddNewToDo = findViewById<View>(R.id.fbAddNewToDo)

        rvToDoItems = findViewById(R.id.rvToDoItems)
        rvToDoItems.layoutManager = LinearLayoutManager(this)
        rvToDoItems.setHasFixedSize(true)
        toDoArrayList = arrayListOf<ToDoModel>()

        getUserData()

        fbAddNewToDo.setOnClickListener {
            AddNewToDoDialog(this).show() //show add to-do dialog
        }

    }

    private fun getUserData() {
        databaseReference = FirebaseDatabase.getInstance().getReference("Users")

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (userSnapshot in snapshot.children) {
                        val user = userSnapshot.getValue(ToDoModel::class.java)
                        toDoArrayList.add(user!!)
                    }
                    rvToDoItems.adapter = ToDoAdapter(toDoArrayList)
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }
}