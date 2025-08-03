package com.redux.todo.data
import com.redux.todo.data.models.Todo
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class TodoRepositoryTest {
    private lateinit var repository: TodoRepository
    private lateinit var mockTodoService: MockTodoService

    @Before
    fun setup() {
        mockTodoService = MockTodoService()
        repository = TodoRepository(mockTodoService)
    }

    @Test
    fun getTodos() = runTest {
        val result = repository.getTodos()
        assertTrue(result.isEmpty())
    }

    @Test
    fun addTodo() = runTest {
        val newTodo = Todo(
            id = "1",
            title = "Test",
            description = "Test description",
            isCompleted = false
        )

        val result = repository.addTodo(newTodo)

        assertEquals(newTodo, result)
        assertTrue(repository.getTodos().contains(newTodo))
    }

    @Test
    fun updateTodo() = runTest {
        val originalTodo = Todo(
            id = "2",
            title = "Original",
            description = "Original description",
            isCompleted = false
        )
        repository.addTodo(originalTodo)

        val updatedTodo = originalTodo.copy(
            title = "Updated",
            isCompleted = true
        )

        val result = repository.updateTodo(updatedTodo)

        assertEquals(updatedTodo, result)
        val todos = repository.getTodos()
        assertTrue(todos.any { it.id == "2" && it.title == "Updated" })
    }

    @Test
    fun deleteTodo() = runTest {
        val todo = Todo(
            id = "3",
            title = "To delete",
            description = "Delete me",
            isCompleted = false
        )
        repository.addTodo(todo)

        val result = repository.deleteTodo(todo.id)

        assertTrue(result)
        assertFalse(repository.getTodos().any { it.id == "3" })
    }

    @Test
    fun deleteTodoNonExistent() = runTest {
        val result = repository.deleteTodo("non-existent")

        assertFalse(result)
    }
}