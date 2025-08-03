package com.redux.todo.data.models

data class Todo(
    val id: String,
    val title: String,
    val description: String,
    val isCompleted: Boolean,
    val createdAt: Long = System.currentTimeMillis()
)