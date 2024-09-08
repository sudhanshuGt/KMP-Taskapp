package com.taskapp.database

import kotlin.Long
import kotlin.String

public data class LoginState(
  public val id: Long,
  public val userId: Long
) {
  public override fun toString(): String = """
  |LoginState [
  |  id: $id
  |  userId: $userId
  |]
  """.trimMargin()
}
