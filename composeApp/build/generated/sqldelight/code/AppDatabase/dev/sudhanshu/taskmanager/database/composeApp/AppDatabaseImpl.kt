package dev.sudhanshu.taskmanager.database.composeApp

import com.squareup.sqldelight.Query
import com.squareup.sqldelight.TransacterImpl
import com.squareup.sqldelight.`internal`.copyOnWriteList
import com.squareup.sqldelight.db.SqlCursor
import com.squareup.sqldelight.db.SqlDriver
import com.taskapp.database.LoginState
import com.taskapp.database.LoginStateQueries
import com.taskapp.database.Task
import com.taskapp.database.TaskQueries
import com.taskapp.database.User
import com.taskapp.database.UserQueries
import dev.sudhanshu.taskmanager.database.AppDatabase
import kotlin.Any
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlin.Unit
import kotlin.collections.MutableList
import kotlin.reflect.KClass

internal val KClass<AppDatabase>.schema: SqlDriver.Schema
  get() = AppDatabaseImpl.Schema

internal fun KClass<AppDatabase>.newInstance(driver: SqlDriver): AppDatabase =
    AppDatabaseImpl(driver)

private class AppDatabaseImpl(
  driver: SqlDriver
) : TransacterImpl(driver), AppDatabase {
  public override val loginStateQueries: LoginStateQueriesImpl = LoginStateQueriesImpl(this, driver)

  public override val taskQueries: TaskQueriesImpl = TaskQueriesImpl(this, driver)

  public override val userQueries: UserQueriesImpl = UserQueriesImpl(this, driver)

  public object Schema : SqlDriver.Schema {
    public override val version: Int
      get() = 1

    public override fun create(driver: SqlDriver): Unit {
      driver.execute(null, """
          |CREATE TABLE LoginState (
          |    id INTEGER PRIMARY KEY AUTOINCREMENT,
          |    userId INTEGER NOT NULL,
          |    FOREIGN KEY(userId) REFERENCES User(id) ON DELETE CASCADE
          |)
          """.trimMargin(), 0)
      driver.execute(null, """
          |CREATE TABLE Task (
          |    id INTEGER PRIMARY KEY AUTOINCREMENT,
          |    title TEXT NOT NULL,
          |    description TEXT,
          |    dueDate TEXT,
          |    status INTEGER NOT NULL
          |)
          """.trimMargin(), 0)
      driver.execute(null, """
          |CREATE TABLE User (
          |    id INTEGER PRIMARY KEY AUTOINCREMENT,
          |    email TEXT NOT NULL UNIQUE,
          |    password TEXT NOT NULL
          |)
          """.trimMargin(), 0)
    }

    public override fun migrate(
      driver: SqlDriver,
      oldVersion: Int,
      newVersion: Int
    ): Unit {
    }
  }
}

private class LoginStateQueriesImpl(
  private val database: AppDatabaseImpl,
  private val driver: SqlDriver
) : TransacterImpl(driver), LoginStateQueries {
  internal val selectAll: MutableList<Query<*>> = copyOnWriteList()

  public override fun <T : Any> selectAll(mapper: (id: Long, userId: Long) -> T): Query<T> =
      Query(-1734894513, selectAll, driver, "LoginState.sq", "selectAll",
      "SELECT * FROM LoginState ORDER BY id ASC LIMIT 1") { cursor ->
    mapper(
      cursor.getLong(0)!!,
      cursor.getLong(1)!!
    )
  }

  public override fun selectAll(): Query<LoginState> = selectAll { id, userId ->
    LoginState(
      id,
      userId
    )
  }

  public override fun insertLoginState(userId: Long): Unit {
    driver.execute(-967474761, """INSERT INTO LoginState(userId) VALUES (?)""", 1) {
      bindLong(1, userId)
    }
    notifyQueries(-967474761, {database.loginStateQueries.selectAll})
  }

  public override fun deleteAll(): Unit {
    driver.execute(-377628352, """DELETE FROM LoginState""", 0)
    notifyQueries(-377628352, {database.loginStateQueries.selectAll})
  }
}

private class TaskQueriesImpl(
  private val database: AppDatabaseImpl,
  private val driver: SqlDriver
) : TransacterImpl(driver), TaskQueries {
  internal val selectAllTasks: MutableList<Query<*>> = copyOnWriteList()

  internal val selectTaskById: MutableList<Query<*>> = copyOnWriteList()

  public override fun <T : Any> selectAllTasks(mapper: (
    id: Long,
    title: String,
    description: String?,
    dueDate: String?,
    status: Long
  ) -> T): Query<T> = Query(-1279043044, selectAllTasks, driver, "Task.sq", "selectAllTasks",
      "SELECT * FROM Task") { cursor ->
    mapper(
      cursor.getLong(0)!!,
      cursor.getString(1)!!,
      cursor.getString(2),
      cursor.getString(3),
      cursor.getLong(4)!!
    )
  }

  public override fun selectAllTasks(): Query<Task> = selectAllTasks { id, title, description,
      dueDate, status ->
    Task(
      id,
      title,
      description,
      dueDate,
      status
    )
  }

  public override fun <T : Any> selectTaskById(id: Long, mapper: (
    id: Long,
    title: String,
    description: String?,
    dueDate: String?,
    status: Long
  ) -> T): Query<T> = SelectTaskByIdQuery(id) { cursor ->
    mapper(
      cursor.getLong(0)!!,
      cursor.getString(1)!!,
      cursor.getString(2),
      cursor.getString(3),
      cursor.getLong(4)!!
    )
  }

  public override fun selectTaskById(id: Long): Query<Task> = selectTaskById(id) { id_, title,
      description, dueDate, status ->
    Task(
      id_,
      title,
      description,
      dueDate,
      status
    )
  }

  public override fun insertTask(
    title: String,
    description: String?,
    dueDate: String?,
    status: Long
  ): Unit {
    driver.execute(2112305137,
        """INSERT INTO Task (title, description, dueDate, status) VALUES (?, ?, ?, ?)""", 4) {
      bindString(1, title)
      bindString(2, description)
      bindString(3, dueDate)
      bindLong(4, status)
    }
    notifyQueries(2112305137, {database.taskQueries.selectAllTasks +
        database.taskQueries.selectTaskById})
  }

  public override fun updateTask(
    title: String,
    description: String?,
    dueDate: String?,
    status: Long,
    id: Long
  ): Unit {
    driver.execute(850208257, """
    |UPDATE Task
    |SET title = ?, description = ?, dueDate = ?, status = ?
    |WHERE id = ?
    """.trimMargin(), 5) {
      bindString(1, title)
      bindString(2, description)
      bindString(3, dueDate)
      bindLong(4, status)
      bindLong(5, id)
    }
    notifyQueries(850208257, {database.taskQueries.selectAllTasks +
        database.taskQueries.selectTaskById})
  }

  public override fun deleteTaskById(id: Long): Unit {
    driver.execute(2107398229, """DELETE FROM Task WHERE id = ?""", 1) {
      bindLong(1, id)
    }
    notifyQueries(2107398229, {database.taskQueries.selectAllTasks +
        database.taskQueries.selectTaskById})
  }

  public override fun deleteAllTasks(): Unit {
    driver.execute(10652619, """DELETE FROM Task""", 0)
    notifyQueries(10652619, {database.taskQueries.selectAllTasks +
        database.taskQueries.selectTaskById})
  }

  private inner class SelectTaskByIdQuery<out T : Any>(
    public val id: Long,
    mapper: (SqlCursor) -> T
  ) : Query<T>(selectTaskById, mapper) {
    public override fun execute(): SqlCursor = driver.executeQuery(817702566,
        """SELECT * FROM Task WHERE id = ?""", 1) {
      bindLong(1, id)
    }

    public override fun toString(): String = "Task.sq:selectTaskById"
  }
}

private class UserQueriesImpl(
  private val database: AppDatabaseImpl,
  private val driver: SqlDriver
) : TransacterImpl(driver), UserQueries {
  internal val getUserByEmail: MutableList<Query<*>> = copyOnWriteList()

  internal val getUserEmail: MutableList<Query<*>> = copyOnWriteList()

  public override fun <T : Any> getUserByEmail(email: String, mapper: (
    id: Long,
    email: String,
    password: String
  ) -> T): Query<T> = GetUserByEmailQuery(email) { cursor ->
    mapper(
      cursor.getLong(0)!!,
      cursor.getString(1)!!,
      cursor.getString(2)!!
    )
  }

  public override fun getUserByEmail(email: String): Query<User> = getUserByEmail(email) { id,
      email_, password ->
    User(
      id,
      email_,
      password
    )
  }

  public override fun getUserEmail(): Query<String> = Query(1474103668, getUserEmail, driver,
      "User.sq", "getUserEmail", "SELECT email FROM User ORDER BY id ASC LIMIT 1") { cursor ->
    cursor.getString(0)!!
  }

  public override fun insertUser(email: String, password: String): Unit {
    driver.execute(225522173, """INSERT INTO User(email, password) VALUES (?, ?)""", 2) {
      bindString(1, email)
      bindString(2, password)
    }
    notifyQueries(225522173, {database.userQueries.getUserByEmail +
        database.userQueries.getUserEmail})
  }

  public override fun deleteData(): Unit {
    driver.execute(1023422414, """DELETE  FROM User""", 0)
    notifyQueries(1023422414, {database.loginStateQueries.selectAll +
        database.userQueries.getUserByEmail + database.userQueries.getUserEmail})
  }

  private inner class GetUserByEmailQuery<out T : Any>(
    public val email: String,
    mapper: (SqlCursor) -> T
  ) : Query<T>(getUserByEmail, mapper) {
    public override fun execute(): SqlCursor = driver.executeQuery(1224676701,
        """SELECT * FROM User WHERE email = ?""", 1) {
      bindString(1, email)
    }

    public override fun toString(): String = "User.sq:getUserByEmail"
  }
}
