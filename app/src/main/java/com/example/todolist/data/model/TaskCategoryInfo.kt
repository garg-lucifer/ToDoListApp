package com.example.todolist.data.model

import androidx.room.Embedded
import androidx.room.Relation
import java.io.Serializable

data class TaskCategoryInfo(

    @Embedded val taskInfo: TaskInfo,
    @Relation(
        parentColumn = "category",
        entityColumn = "categoryInformation"
    )
    val categoryInfo: List<CategoryInfo>
) : Serializable
