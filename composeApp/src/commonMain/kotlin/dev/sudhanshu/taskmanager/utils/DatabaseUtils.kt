package dev.sudhanshu.taskmanager.utils

import com.squareup.sqldelight.db.SqlDriver
import dev.sudhanshu.taskmanager.database.AppDatabase

class DatabaseUtils {

    fun createDatabase(driver: SqlDriver): AppDatabase {
        return AppDatabase(driver)
    }

}