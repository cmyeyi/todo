package com.redux.todo.redux

import com.redux.todo.data.models.Todo

data class AppState(
    val todos: List<Todo> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val selectedTodo: Todo? = null
)