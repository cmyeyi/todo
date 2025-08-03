package com.redux.todo.redux

import com.redux.todo.data.models.Todo

sealed class TodoAction {
    data object LoadTodos : TodoAction()
    data class TodosLoaded(val todos: List<Todo>) : TodoAction()
    data class LoadTodosFailed(val error: String) : TodoAction()
    data class AddTodo(val todo: Todo) : TodoAction()
    data class UpdateTodo(val todo: Todo) : TodoAction()
    data class DeleteTodo(val id: String) : TodoAction()
    data class SelectTodo(val todo: Todo) : TodoAction()
}