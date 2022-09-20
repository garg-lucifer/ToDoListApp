package com.example.todolist.presentation.di

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class ToDoListApplication : Application(){
    @Inject
    lateinit var sharedPreferences: SharedPreferences
    override fun onCreate() {
        super.onCreate()
        if(sharedPreferences.getBoolean("dark_theme", false))
             AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        else AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

    }
}