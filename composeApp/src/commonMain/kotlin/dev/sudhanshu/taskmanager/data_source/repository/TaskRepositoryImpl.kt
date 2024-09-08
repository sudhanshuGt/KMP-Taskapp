package dev.sudhanshu.taskmanager.data_source.repository

import com.taskapp.database.Task
import com.taskapp.database.TaskQueries
import dev.sudhanshu.taskmanager.database.AppDatabase


class TaskRepositoryImpl(
    private val appDatabase: AppDatabase
) : TaskRepository {

    private val taskQueries : TaskQueries = appDatabase.taskQueries

    override suspend fun getAllTasks(): List<Task> {
        return taskQueries.selectAllTasks().executeAsList().map {
            Task(
                id = it.id,
                title = it.title,
                description = it.description,
                dueDate = it.dueDate,
                status = it.status
            )
        }
    }

    override suspend fun insertTask(title: String, description: String?, dueDate: String?, status: Int) {
        taskQueries.insertTask(title, description, dueDate, status.toLong())
    }

    override suspend fun updateTask(id: Long, title: String, description: String?, dueDate: String?, status: Int) {
        taskQueries.updateTask(title, description, dueDate, status.toLong(), id)
    }

    override suspend fun deleteTaskById(id: Long) {
        taskQueries.deleteTaskById(id)
    }

    override suspend fun deleteAllTasks() {
        taskQueries.deleteAllTasks()
    }
}
