package com.example.todolist

data class ToDoModel(
    val title: String ?= null,
    val checked: Boolean ?= null
)