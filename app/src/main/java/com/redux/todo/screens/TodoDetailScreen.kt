package com.redux.todo.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.redux.todo.data.TodoRepository
import com.redux.todo.redux.Store
import com.redux.todo.redux.TodoAction
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoDetailScreen(
    store: Store,
    navController: NavController,
    repository: TodoRepository
) {
    val state by store.state.collectAsState()
    val todo = state.selectedTodo ?: run {
        navController.popBackStack()
        return
    }

    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Todo Details") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        scope.launch {
                            try {
                                val deleted = repository.deleteTodo(todo.id)
                                if (deleted) {
                                    store.dispatch( TodoAction.DeleteTodo(todo.id))
                                    navController.popBackStack()
                                }
                            } catch (e: Exception) {
                                store.dispatch(TodoAction.LoadTodosFailed(e.message ?: "Delete failed"))
                            }
                        }
                    }) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {
            Text(
                text = todo.title,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = todo.description,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Text(
                text = "Created: ${SimpleDateFormat("MMM dd, yyyy HH:mm").format(Date(todo.createdAt))}",
                style = MaterialTheme.typography.bodySmall
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Checkbox(
                    checked = todo.isCompleted,
                    onCheckedChange = { isChecked ->
                        scope.launch {
                            try {
                                val updated = repository.updateTodo(todo.copy(isCompleted = isChecked))
                                store.dispatch(TodoAction.UpdateTodo(updated))
                            } catch (e: Exception) {
                                store.dispatch(TodoAction.LoadTodosFailed(e.message ?: "Update failed"))
                            }
                        }
                    }
                )
                Text(
                    modifier = Modifier.padding(start = 8.dp),
                    text = if (todo.isCompleted) "Completed" else "Pending",
                    fontSize = 12.sp
                )
            }
        }
    }
}