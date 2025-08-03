package com.redux.todo.data

import com.redux.todo.data.db.TodoDao
import com.redux.todo.data.models.Todo
import kotlinx.coroutines.delay
import javax.inject.Inject

interface TodoService {
    suspend fun getTodos(): List<Todo>
    suspend fun addTodo(todo: Todo): Todo
    suspend fun updateTodo(todo: Todo): Todo
    suspend fun deleteTodo(id: String): Boolean
}

class RoomTodoService @Inject constructor(private val todoDao: TodoDao) : TodoService {
    override suspend fun getTodos(): List<Todo> {
        return todoDao.getAll()
    }

    override suspend fun addTodo(todo: Todo): Todo {
        todoDao.insertOrUpdate(todo)
        return todo
    }

    override suspend fun updateTodo(todo: Todo): Todo {
        todoDao.insertOrUpdate(todo)
        return todo
    }

    override suspend fun deleteTodo(id: String): Boolean {
        val deleteRows = todoDao.deleteById(id)
        return deleteRows > 0
    }

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