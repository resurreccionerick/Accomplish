package com.example.todolist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ToDoAdapter(private val toDoList: ArrayList<ToDoModel>) :
    RecyclerView.Adapter<ToDoAdapter.ToDoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_todo, parent, false)
        return ToDoViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ToDoViewHolder, position: Int) {
        val curToDo = toDoList[position]

        holder.title.text = curToDo.title

        if(curToDo.checked == true){
            holder.checkBox.isChecked = true
        }

    }

    override fun getItemCount(): Int {
        return toDoList.size
    }

    class ToDoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.tvToDoTitle)
        val checkBox: CheckBox = itemView.findViewById(R.id.cbDone)
    }
}