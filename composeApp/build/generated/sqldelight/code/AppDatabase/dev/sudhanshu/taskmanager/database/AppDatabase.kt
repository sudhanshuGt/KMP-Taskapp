package dev.sudhanshu.taskmanager.database

import com.squareup.sqldelight.Transacter
import com.squareup.sqldelight.db.SqlDriver
import com.taskapp.database.LoginStateQueries
import com.taskapp.database.TaskQueries
import com.taskapp.database.UserQueries
import dev.sudhanshu.taskmanager.database.composeApp.newInstance
import dev.sudhanshu.taskmanager.database.composeApp.schema

public interface AppDatabase : Transacter {
  public val loginStateQueries: LoginStateQueries

  public val taskQueries: TaskQueries

  public val userQueries: UserQueries

  public companion object {
    public val Schema: SqlDriver.Schema
      get() = AppDatabase::class.schema

    public operator fun invoke(driver: SqlDriver): AppDatabase =
        AppDatabase::class.newInstance(driver)
  }
}
