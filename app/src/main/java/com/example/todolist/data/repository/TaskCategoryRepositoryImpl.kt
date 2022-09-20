package com.example.todolist.data.repository

import androidx.lifecycle.LiveData
import com.example.todolist.data.db.TaskCategoryDao
import com.example.todolist.domain.TaskCategoryRepository
import com.example.todolist.data.model.CategoryInfo
import com.example.todolist.data.model.NoOfTaskForEachCategory
import com.example.todolist.data.model.TaskCategoryInfo
import com.example.todolist.data.model.TaskInfo
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

class TaskCategoryRepositoryImpl @Inject constructor(private val taskCategoryDao: TaskCategoryDao) :
    TaskCategoryRepository {

    override suspend fun updateTaskStatus(task: TaskInfo) : Int{
        return taskCategoryDao.updateTaskStatus(task)
    }

    override suspend fun deleteTask(task: TaskInfo) {
        taskCategoryDao.deleteTask(task)
    }

    override suspend fun insertTaskAndCategory(taskInfo: TaskInfo, categoryInfo: CategoryInfo) {
        taskCategoryDao.insertTaskAndCategory(taskInfo, categoryInfo)
    }

    override suspend fun deleteTaskAndCategory(taskInfo: TaskInfo, categoryInfo: CategoryInfo) {
        taskCategoryDao.deleteTaskAndCategory(taskInfo, categoryInfo)
    }

    override suspend fun updateTaskAndAddDeleteCategory(
        taskInfo: TaskInfo,
        categoryInfoAdd: CategoryInfo,
        categoryInfoDelete: CategoryInfo
    ) {
        taskCategoryDao.updateTaskAndAddDeleteCategory(taskInfo, categoryInfoAdd, categoryInfoDelete)
    }

    override suspend fun updateTaskAndAddCategory(taskInfo: TaskInfo, categoryInfo: CategoryInfo) {
        taskCategoryDao.updateTaskAndAddCategory(taskInfo, categoryInfo)
    }

    override fun getUncompletedTask(): LiveData<List<TaskCategoryInfo>> = taskCategoryDao.getUncompletedTask()
    override fun getCompletedTask(): LiveData<List<TaskCategoryInfo>> = taskCategoryDao.getCompletedTask()
    override fun getUncompletedTaskOfCategory(category: String): LiveData<List<TaskCategoryInfo>> = taskCategoryDao.getUncompletedTaskOfCategory(category)
    override fun getCompletedTaskOfCategory(category: String): LiveData<List<TaskCategoryInfo>> = taskCategoryDao.getCompletedTaskOfCategory(category)
    override fun getNoOfTaskForEachCategory(): LiveData<List<NoOfTaskForEachCategory>> = taskCategoryDao.getNoOfTaskForEachCategory()
    override fun getCategories(): LiveData<List<CategoryInfo>> = taskCategoryDao.getCategories()
    override suspend fun getCountOfCategory(category: String): Int = taskCategoryDao.getCountOfCategory(category)
    override suspend fun getActiveAlarms(currentTime: Date): List<TaskInfo> {
        var list: List<TaskInfo>
        coroutineScope {
            list = withContext(IO){taskCategoryDao.getActiveAlarms(currentTime)}
        }
        return list
    }
}