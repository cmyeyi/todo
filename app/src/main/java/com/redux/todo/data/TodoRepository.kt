package com.redux.todo.data

import com.redux.todo.data.models.Todo
import javax.inject.Inject

class TodoRepository @Inject constructor(private val service: TodoService) {
    suspend fun getTodos(): List<Todo> = service.getTodos()
    suspend fun addTodo(todo: Todo): Todo = service.addTodo(todo)
    suspend fun updateTodo(todo: Todo): Todo = service.updateTodo(todo)
    suspend fun deleteTodo(id: String): Boolean = service.deleteTodo(id)
}