package com.example.accomplish_to_do_app.fragments

import com.example.accomplish_to_do_app.utils.ToDoAdapter
import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.accomplish_to_do_app.R
import com.example.accomplish_to_do_app.databinding.FragmentTaskBinding
import com.example.accomplish_to_do_app.utils.ToDoModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class TaskFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var databaseRef: DatabaseReference
    private lateinit var navController: NavController
    private lateinit var binding: FragmentTaskBinding
    private lateinit var adapter: ToDoAdapter
    private lateinit var mList : ArrayList<ToDoModel>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentTaskBinding.inflate(inflater, container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(view)
        addTask()

        val toolbar = activity?.findViewById<Toolbar>(R.id.toolbar)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)

        // Explicitly setHasOptionsMenu(true) and inflate the menu again
        setHasOptionsMenu(true)
        toolbar?.inflateMenu(R.menu.todo_menu)
    }

    private fun init(view: View) {
        navController = Navigation.findNavController(view)
        auth = FirebaseAuth.getInstance()
        databaseRef = FirebaseDatabase.getInstance().reference
            .child("Tasks").child(auth.currentUser?.uid.toString())

        binding.rvTask.setHasFixedSize(true)
        binding.rvTask.layoutManager = LinearLayoutManager(context)

        getDataFromFirebase()

        mList = ArrayList<ToDoModel>()
        adapter = ToDoAdapter(mList, navController)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.todo_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_delete_all -> {
                databaseRef.removeValue()

                return true
            }

            R.id.action_logout -> {
                auth.signOut()
                navController.navigate(R.id.action_taskFragment_to_loginFragment) //restart fragment
                return true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }


    private fun getDataFromFirebase() {
        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (userSnapshot in snapshot.children) {
                        val user = userSnapshot.getValue(ToDoModel::class.java)
                        mList.add(user!!)
                    }
                    binding.rvTask.adapter = ToDoAdapter(mList, navController)
                    Log.d("DATAS...", mList.toString())
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    private fun addTask() {
        binding.btnAddTask.setOnClickListener{
            val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.todo_popup, null)

            val dialog = AlertDialog.Builder(requireContext())
                .setView(dialogView)
                .create()

            val txtDialogAddTodo = dialogView.findViewById<EditText>(R.id.txtDialogAddTodo)
            val btnDialogAddNewTodo = dialogView.findViewById<Button>(R.id.btnDialogAddNewTodo)


            btnDialogAddNewTodo.setOnClickListener {
                val value = txtDialogAddTodo.text.toString()

                val taskRef = databaseRef.push() // Generate a new child node ID
                val taskId = taskRef.key // Get the generated ID

                val data = HashMap<String, Any>()
                data["id"] = taskId.toString()
                data["todo"] = value

                if (value.isNotEmpty()) {
                    taskRef.setValue(data).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            txtDialogAddTodo.setText("")
                            Toast.makeText(context, "Todo Added", Toast.LENGTH_SHORT).show()
                            dialog.dismiss()

                            mList.clear()
                            getDataFromFirebase() //refresh recycler view
                        } else {
                            Toast.makeText(context, task.exception?.message.toString(), Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(context, "Please enter a todo", Toast.LENGTH_SHORT).show()
                }
            }
            dialog.show()
        }
    }

}