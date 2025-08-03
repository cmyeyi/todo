package com.redux.todo.redux

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class Store @Inject constructor(
    private val reducer: (AppState, Any) -> AppState,
    initialState: AppState
) : ViewModel() {
    private val _state = MutableStateFlow(initialState)
    val state: StateFlow<AppState> = _state

    suspend fun dispatch(action: Any) {
        _state.value = reducer(_state.value, action)
    }
}