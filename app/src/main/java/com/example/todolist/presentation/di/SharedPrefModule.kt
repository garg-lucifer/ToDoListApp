package com.example.todolist.presentation.di

import android.app.Application
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SharedPrefModule {
    @Provides
    @Singleton
    fun provideTaskDatabase(app: Application): SharedPreferences {
        return app.getSharedPreferences("settings", MODE_PRIVATE)
    }
}