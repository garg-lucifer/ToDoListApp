package com.example.todolist.presentation.di
import android.app.Application
import androidx.room.Room
import com.example.todolist.data.db.TaskCategoryDao
import com.example.todolist.data.db.TaskDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideTaskDatabase(app: Application) : TaskDatabase {
        return Room.databaseBuilder(app, TaskDatabase::class.java, "task_db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideTaskCategoryDao(taskDatabase: TaskDatabase): TaskCategoryDao {
        return taskDatabase.getTaskCategoryDao()
    }

}