package dev.sudhanshu.taskmanager.data_source.repository

import com.taskapp.database.Task


interface TaskRepository {
    suspend fun getAllTasks(): List<Task>
    suspend fun insertTask(title: String, description: String?, dueDate: String?, status: Int)
    suspend fun updateTask(id: Long, title: String, description: String?, dueDate: String?, status: Int)
    suspend fun deleteTaskById(id: Long)
    suspend fun deleteAllTasks()
}