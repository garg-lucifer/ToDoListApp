package com.example.todolist.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.todolist.data.model.CategoryInfo
import com.example.todolist.data.model.NoOfTaskForEachCategory
import com.example.todolist.data.model.TaskCategoryInfo
import com.example.todolist.data.model.TaskInfo
import java.util.*

@Dao
interface TaskCategoryDao {
    @Insert
    suspend fun insertTask(task : TaskInfo) : Long

    @Update
    suspend fun updateTaskStatus(task: TaskInfo) : Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(categoryInfo: CategoryInfo) : Long

    @Delete
    suspend fun deleteTask(task: TaskInfo)

    @Delete
    suspend fun deleteCategory(categoryInfo: CategoryInfo)

    @Transaction
    suspend fun insertTaskAndCategory(taskInfo: TaskInfo, categoryInfo: CategoryInfo){
        insertTask(taskInfo)
        insertCategory(categoryInfo)
    }

    @Transaction
    suspend fun updateTaskAndAddCategory(taskInfo: TaskInfo, categoryInfo: CategoryInfo){
        updateTaskStatus(taskInfo)
        insertCategory(categoryInfo)
    }

    @Transaction
    suspend fun updateTaskAndAddDeleteCategory(taskInfo: TaskInfo, categoryInfoAdd: CategoryInfo, categoryInfoDelete: CategoryInfo){
        updateTaskStatus(taskInfo)
        insertCategory(categoryInfoAdd)
        deleteCategory(categoryInfoDelete)
    }

    @Transaction
    suspend fun deleteTaskAndCategory(taskInfo: TaskInfo, categoryInfo: CategoryInfo){
        deleteTask(taskInfo)
        deleteCategory(categoryInfo)
    }

    @Transaction
    @Query("SELECT * " +
            "FROM taskInfo " +
            "WHERE status = 0 " +
            "ORDER BY date")
    fun getUncompletedTask(): LiveData<List<TaskCategoryInfo>>

    @Transaction
    @Query("SELECT * " +
            "FROM taskInfo " +
            "WHERE status = 1 " +
            "ORDER BY date")
    fun getCompletedTask(): LiveData<List<TaskCategoryInfo>>

    @Transaction
    @Query("SELECT * " +
            "FROM taskInfo " +
            "WHERE status = 0 " +
            "AND category =:category " +
            "ORDER BY date")
    fun getUncompletedTaskOfCategory(category: String): LiveData<List<TaskCategoryInfo>>

    @Transaction
    @Query("SELECT * " +
            "FROM taskInfo " +
            "WHERE status = 1 " +
            "AND category = :category " +
            "ORDER BY date")
    fun getCompletedTaskOfCategory(category: String): LiveData<List<TaskCategoryInfo>>

    @Query("SELECT " +
            "taskInfo.category as category," +
            "Count(*) as count, " +
            "categoryInfo.color as color  " +
            "FROM taskInfo, categoryInfo " +
            "WHERE taskInfo.category == categoryInfo.categoryInformation " +
            "AND " +
            "taskInfo.status = 0 " +
            "GROUP BY category " +
            "ORDER BY count DESC, category")

    fun getNoOfTaskForEachCategory(): LiveData<List<NoOfTaskForEachCategory>>

    @Query("SELECT * " +
            "FROM categoryInfo")
    fun getCategories(): LiveData<List<CategoryInfo>>

    @Query("SELECT * " +
            "FROM taskInfo")
    fun getTasks(): LiveData<List<TaskInfo>>

    @Query("SELECT COUNT(*) " +
            "FROM taskInfo " +
            "WHERE category = :category ")
    fun getCountOfCategory(category: String) : Int

    @Query("SELECT * " +
            "FROM taskInfo " +
            "WHERE status = 0 " +
            "AND date > :currentTime")
    fun getActiveAlarms(currentTime : Date) : List<TaskInfo>

}