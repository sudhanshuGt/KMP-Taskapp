package dev.sudhanshu.taskmanager

import androidx.compose.ui.window.ComposeUIViewController
import com.squareup.sqldelight.db.SqlDriver
import dev.sudhanshu.taskmanager.utils.DatabaseUtils

import dev.sudhanshu.taskmanager.data_source.repository.AuthRepository
import dev.sudhanshu.taskmanager.data_source.repository.TaskRepositoryImpl
import dev.sudhanshu.taskmanager.view.auth.AuthViewModel
import dev.sudhanshu.taskmanager.view.task.TaskViewModel


fun MainViewController() = ComposeUIViewController {


    // Creating the database driver
    val driverFactory = DatabaseDriverFactory()
    val driver: SqlDriver = driverFactory.createDriver()

    // Initializing DatabaseUtils and create the database instance
    val databaseUtils = DatabaseUtils()
    val database = databaseUtils.createDatabase(driver)

    // Initializing AuthRepository and AuthViewModel
    val authRepository = AuthRepository(database)
    val authViewModel = AuthViewModel(authRepository)

    val taskRepository = TaskRepositoryImpl(database)
    val taskViewModel  = TaskViewModel(taskRepository)
    val hapticFeedback = HapticFeedback()
    val biometricAuthenticator = BiometricAuthenticator()
    App(viewModel = authViewModel, biometricAuthenticator, taskViewModel, hapticFeedback)
}
