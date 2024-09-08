package com.taskapp.database

import com.squareup.sqldelight.Query
import com.squareup.sqldelight.Transacter
import kotlin.Any
import kotlin.Long
import kotlin.Unit

public interface LoginStateQueries : Transacter {
  public fun <T : Any> selectAll(mapper: (id: Long, userId: Long) -> T): Query<T>

  public fun selectAll(): Query<LoginState>

  public fun insertLoginState(userId: Long): Unit

  public fun deleteAll(): Unit
}
