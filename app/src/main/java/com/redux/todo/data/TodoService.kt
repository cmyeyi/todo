package com.redux.todo.data

import com.redux.todo.data.models.Todo
import kotlinx.coroutines.delay

interface TodoService {
    suspend fun getTodos(): List<Todo>
    suspend fun addTodo(todo: Todo): Todo
    suspend fun updateTodo(todo: Todo): Todo
    suspend fun deleteTodo(id: String): Boolean
}

class MockTodoService : TodoService {
    private val todos = mutableListOf<Todo>()

    override suspend fun getTodos(): List<Todo> {
        delay(500)
        return todos.toList()
    }

    override suspend fun addTodo(todo: Todo): Todo {
        delay(300)
        todos.add(todo)
        return todo
    }

    override suspend fun updateTodo(todo: Todo): Todo {
        delay(300)
        val index = todos.indexOfFirst { it.id == todo.id }
        if (index != -1) {
            todos[index] = todo
        }
        return todo
    }

    override suspend fun deleteTodo(id: String): Boolean {
        delay(300)
        return todos.removeIf { it.id == id }
    }
}