package com.taskapp.database

import kotlin.Long
import kotlin.String

public data class User(
  public val id: Long,
  public val email: String,
  public val password: String
) {
  public override fun toString(): String = """
  |User [
  |  id: $id
  |  email: $email
  |  password: $password
  |]
  """.trimMargin()
}
