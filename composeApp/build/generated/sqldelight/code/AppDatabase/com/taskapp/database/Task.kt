package com.taskapp.database

import kotlin.Long
import kotlin.String

public data class Task(
  public val id: Long,
  public val title: String,
  public val description: String?,
  public val dueDate: String?,
  public val status: Long
) {
  public override fun toString(): String = """
  |Task [
  |  id: $id
  |  title: $title
  |  description: $description
  |  dueDate: $dueDate
  |  status: $status
  |]
  """.trimMargin()
}
