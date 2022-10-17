package com.example.todolist

import android.app.Dialog
import android.os.Bundle
import android.view.Window
import android.content.Context
import android.util.Log
import android.widget.Button
import android.widget.EditText
import kotlinx.android.synthetic.main.add_todo_dialog.*

class AddNewToDoDialog(context: Context) : Dialog(context) {

    init {
        setCancelable(true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.add_todo_dialog)

        val txtToDoTitle: EditText = findViewById(R.id.txtToDoTitle)
        val btnAdd: Button = findViewById(R.id.btnAdd)

        btnAdd.setOnClickListener {
            val title = txtToDoTitle.text.toString()
            txtToDoTitle.text.clear()
        }
    }
}