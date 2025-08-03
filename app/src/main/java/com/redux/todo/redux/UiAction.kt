package com.redux.todo.redux

sealed class UiAction {
    data object ClearError : UiAction()
}