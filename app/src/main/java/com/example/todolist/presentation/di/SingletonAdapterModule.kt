package com.example.todolist.presentation.di

import com.example.todolist.presentation.adapter.CategoryAdapter
import com.example.todolist.presentation.adapter.TasksAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SingletonAdapterModule {
    @Provides
    @Singleton
    @Named("base_fragment")
    fun provideTaskAdapterToBaseFragment() = TasksAdapter()

    @Provides
    @Singleton
    @Named("completed_task_fragment")
    fun provideTaskAdapterToCompletedTasksFragment() = TasksAdapter()

    @Provides
    @Singleton
    fun provideCategoryAdapter() = CategoryAdapter()

}