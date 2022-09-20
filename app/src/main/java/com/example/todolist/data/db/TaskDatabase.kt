package com.example.todolist.data.db

import androidx.room.*
import com.example.todolist.data.model.CategoryInfo
import com.example.todolist.data.model.TaskInfo

@Database(entities = [TaskInfo::class, CategoryInfo::class], version = 1)
@TypeConverters(DateConverter::class)
abstract class TaskDatabase : RoomDatabase() {
    abstract fun getTaskCategoryDao() : TaskCategoryDao
}