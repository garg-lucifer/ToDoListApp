package com.example.todolist.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "categoryInfo")
data class CategoryInfo(
    @PrimaryKey
    var categoryInformation: String,
    var color: String
) : Serializable
