package com.taskapp.database

import com.squareup.sqldelight.Query
import com.squareup.sqldelight.Transacter
import kotlin.Any
import kotlin.Long
import kotlin.String
import kotlin.Unit

public interface UserQueries : Transacter {
  public fun <T : Any> getUserByEmail(email: String, mapper: (
    id: Long,
    email: String,
    password: String
  ) -> T): Query<T>

  public fun getUserByEmail(email: String): Query<User>

  public fun getUserEmail(): Query<String>

  public fun insertUser(email: String, password: String): Unit

  public fun deleteData(): Unit
}
