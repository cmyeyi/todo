package com.redux.todo.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.redux.todo.data.TodoRepository
import com.redux.todo.redux.Store
import com.redux.todo.redux.TodoAction
import com.redux.todo.redux.UiAction
import com.redux.todo.ui.components.TodoItem
import kotlinx.coroutines.launch
import androidx.compose.ui.draw.alpha

@Composable
fun TodoListScreen(store: Store, navController: NavController, repository: TodoRepository) {
    val state by store.state.collectAsState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        scope.launch {
            try {
                store.dispatch(TodoAction.LoadTodos)
                val todos = repository.getTodos()
                store.dispatch(TodoAction.TodosLoaded(todos))
            } catch (e: Exception) {
                store.dispatch(TodoAction.LoadTodosFailed(e.message ?: "Unknown error"))
            }
        }
    }
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (!state.isLoading) { // 加载时禁用点击
                        navController.navigate("addTodo")
                    }
                },
                modifier = if (state.isLoading) Modifier.alpha(0.5f) else Modifier
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Todo")
            }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .alpha(if (state.isLoading) 0.6f else 1f)
            ) {
                items(state.todos) { todo ->
                    TodoItem(
                        todo = todo,
                        onItemClick = {
                            if (!state.isLoading) { // 加载时禁用点击
                                scope.launch {
                                    store.dispatch(TodoAction.SelectTodo(it))
                                    navController.navigate("todoDetail")
                                }
                            }
                        },
                        onToggleComplete = { updatedTodo ->
                            if (!state.isLoading) { // 加载时禁用切换
                                scope.launch {
                                    try {
                                        val result = repository.updateTodo(updatedTodo)
                                        store.dispatch(TodoAction.UpdateTodo(result))
                                    } catch (e: Exception) {
                                        store.dispatch(
                                            TodoAction.LoadTodosFailed(
                                                e.message ?: "Update failed"
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    )
                }
            }

            if (state.isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.2f))
                        .clickable { TODO() },
                    contentAlignment = Alignment.Center
                ) {
                    Surface(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.surface,
                        shadowElevation = 8.dp
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.padding(16.dp),
                            color = MaterialTheme.colorScheme.primary,
                            strokeWidth = 3.dp
                        )
                    }
                }
            }

            state.error?.let { error ->
                Snackbar(
                    modifier = Modifier.align(Alignment.BottomCenter),
                    action = {
                        TextButton(onClick = {
                            scope.launch {
                                store.dispatch(UiAction.ClearError)
                            }
                        }) {
                            Text("Dismiss")
                        }
                    }
                ) {
                    Text(error)
                }
            }
        }
    }
}