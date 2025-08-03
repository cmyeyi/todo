package com.redux.todo.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.redux.todo.data.models.Todo


@Database(entities = [Todo::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun todoDao(): TodoDao

    companion object {
        const val DATABASE_NAME = "todo_database"
    }

}