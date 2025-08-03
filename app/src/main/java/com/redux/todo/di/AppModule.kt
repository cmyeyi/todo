package com.redux.todo.di

import android.content.Context
import androidx.room.Room
import com.redux.todo.data.MockTodoService
import com.redux.todo.data.RoomTodoService
import com.redux.todo.data.TodoRepository
import com.redux.todo.data.TodoService
import com.redux.todo.data.db.AppDatabase
import com.redux.todo.data.db.TodoDao
import com.redux.todo.redux.AppState
import com.redux.todo.redux.Store
import com.redux.todo.redux.appReducer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideTodoService(dao: TodoDao): TodoService = RoomTodoService(dao)//MockTodoService -> RoomTodoService

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, AppDatabase.DATABASE_NAME)
            .build()
    }

    @Provides
    @Singleton
    fun provideTodoDao(database: AppDatabase):TodoDao = database.todoDao()

    @Provides
    @Singleton
    fun provideTodoRepository(service: TodoService): TodoRepository = TodoRepository(service)

    @Provides
    @Singleton
    fun provideAppState(): AppState = AppState()

    @Provides
    @Singleton
    fun provideReducer(): (AppState, Any) -> AppState {
        return ::appReducer
    }

    @Provides
    @Singleton
    fun provideStore(
        reducer: @JvmSuppressWildcards (AppState, Any) -> AppState,
        initialState: AppState
    ): Store {
        return Store(reducer, initialState)
    }
}