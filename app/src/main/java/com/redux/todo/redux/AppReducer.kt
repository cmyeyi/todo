package com.redux.todo.redux

fun appReducer(state: AppState, action: Any): AppState {
    return when (action) {
        is TodoAction.LoadTodos -> state.copy(isLoading = true)
        is TodoAction.TodosLoaded -> state.copy(
            todos = action.todos,
            isLoading = false
        )
        is TodoAction.LoadTodosFailed -> state.copy(
            isLoading = false,
            error = action.error
        )
        is TodoAction.AddTodo -> state.copy(
            todos = state.todos + action.todo
        )
        is TodoAction.UpdateTodo -> state.copy(
            todos = state.todos.map {
                if (it.id == action.todo.id) action.todo else it
            }
        )
        is TodoAction.DeleteTodo -> state.copy(
            todos = state.todos.filter { it.id != action.id }
        )
        is TodoAction.SelectTodo -> state.copy(
            selectedTodo = action.todo
        )
        is UiAction.ClearError -> state.copy(
            error = null
        )
        else -> state
    }
}