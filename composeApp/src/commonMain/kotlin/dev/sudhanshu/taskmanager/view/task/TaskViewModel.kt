package dev.sudhanshu.taskmanager.view.task

import com.taskapp.database.Task
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import dev.sudhanshu.taskmanager.data_source.repository.TaskRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime


class TaskViewModel(
    private val repository: TaskRepository
) : ViewModel() {

    private val _editingTask = MutableStateFlow<Task?>(null)
    val editingTask: StateFlow<Task?> = _editingTask

    fun setEditingTask(task: Task?) {
        _editingTask.value = task
    }

    private val _completedTask = MutableStateFlow<List<Task>>(emptyList())
    val completedTask : StateFlow<List<Task>> = _completedTask

    private val _pendingTask = MutableStateFlow<List<Task>>(emptyList())
    val pendingTask : StateFlow<List<Task>> = _pendingTask

    private val _todayTask = MutableStateFlow<List<Task>>(emptyList())
    val todayTask : StateFlow<List<Task>> = _todayTask

    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks: StateFlow<List<Task>> = _tasks

    init {
        loadTasks()
    }

    private fun loadTasks() {
        viewModelScope.launch {
            val tasksList = repository.getAllTasks()
            _tasks.value = tasksList
        }
    }

    fun addTask(title: String, description: String?, dueDate: String?, status: Int) {
        viewModelScope.launch {
            repository.insertTask(title, description, dueDate, status)
            loadTasks()
        }
    }

    fun updateTask(id: Long, title: String, description: String?, dueDate: String?, status: Int) {
       viewModelScope.launch {
           repository.updateTask(id, title, description, dueDate, status)
           loadTasks()
       }
    }

    fun deleteTask(id: Long) {
        viewModelScope.launch {
            repository.deleteTaskById(id)
            loadTasks()
        }
    }

    fun getTaskById(id: Long): Task? {
        return tasks.value.find { it.id == id }
    }

    fun getCompletedTasks() {
        _completedTask.value = tasks.value.filter { it.status.toInt() == 0 }
    }

    fun getPendingTasks() {
        _pendingTask.value = tasks.value.filter { it.status.toInt() == 1 }
    }

    fun getTasksForCurrentDate() {
        val currentDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        _todayTask.value = tasks.value.filter { task ->
            task.dueDate?.let { dueDateString ->
                try {
                    val taskDate = LocalDate.parse(dueDateString)
                    taskDate == currentDate
                } catch (e: Exception) {
                    false
                }
            } ?: false
        }
    }

    fun getCurrentDate() : String {
        return Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date.toString()

    }

    fun deleteAllTasks() {
        viewModelScope.launch {
            repository.deleteAllTasks()
            loadTasks()
        }
    }
}
