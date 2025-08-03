package com.redux.todo.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.redux.todo.data.TodoRepository
import com.redux.todo.redux.Store
import com.redux.todo.screens.AddTodoScreen
import com.redux.todo.screens.TodoDetailScreen
import com.redux.todo.screens.TodoListScreen

@Composable
fun NavGraph(
    store: Store,
    repository: TodoRepository
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "todoList"
    ) {
        composable("todoList") {
            TodoListScreen(store, navController, repository)
        }
        composable("addTodo") {
            AddTodoScreen(store, navController, repository)
        }
        composable("todoDetail") {
            TodoDetailScreen(store, navController, repository)
        }
    }
}