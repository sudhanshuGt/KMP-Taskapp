package com.taskapp.database

import com.squareup.sqldelight.Query
import com.squareup.sqldelight.Transacter
import kotlin.Any
import kotlin.Long
import kotlin.String
import kotlin.Unit

public interface TaskQueries : Transacter {
  public fun <T : Any> selectAllTasks(mapper: (
    id: Long,
    title: String,
    description: String?,
    dueDate: String?,
    status: Long
  ) -> T): Query<T>

  public fun selectAllTasks(): Query<Task>

  public fun <T : Any> selectTaskById(id: Long, mapper: (
    id: Long,
    title: String,
    description: String?,
    dueDate: String?,
    status: Long
  ) -> T): Query<T>

  public fun selectTaskById(id: Long): Query<Task>

  public fun insertTask(
    title: String,
    description: String?,
    dueDate: String?,
    status: Long
  ): Unit

  public fun updateTask(
    title: String,
    description: String?,
    dueDate: String?,
    status: Long,
    id: Long
  ): Unit

  public fun deleteTaskById(id: Long): Unit

  public fun deleteAllTasks(): Unit
}
