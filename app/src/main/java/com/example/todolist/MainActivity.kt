package com.example.todolist

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var databaseReference: DatabaseReference
    private lateinit var rvToDoItems: RecyclerView
    private lateinit var toDoArrayList: ArrayList<ToDoModel>
    private lateinit var firebaseAuth: FirebaseAuth

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
        firebaseAuth = Firebase.auth
        val user = firebaseAuth.currentUser?.displayName
        databaseReference =
            FirebaseDatabase.getInstance().getReference("Users").child(user.toString())

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (userSnapshot in snapshot.children) {
                        val user = userSnapshot.getValue(ToDoModel::class.java)
                        toDoArrayList.add(user!!)
                    }
                    rvToDoItems.adapter = ToDoAdapter(toDoArrayList, this@MainActivity)
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_logout -> {

                finish()
                startActivity(Intent(this, SplashActivity::class.java))
                firebaseAuth.signOut()

//                var intent = Intent(this, LoginActivity::class.java)
//                startActivity(intent)

                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
