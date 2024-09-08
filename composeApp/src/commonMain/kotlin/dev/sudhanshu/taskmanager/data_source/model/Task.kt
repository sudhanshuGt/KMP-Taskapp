package dev.sudhanshu.taskmanager.data_source.model

data class Task(
    val id: Long,
    val title: String,
    val description: String?,
    val dueDate: String?,
    val status: Int
)
