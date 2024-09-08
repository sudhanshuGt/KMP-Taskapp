package dev.sudhanshu.taskmanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import dev.sudhanshu.taskmanager.data_source.repository.AuthRepository
import dev.sudhanshu.taskmanager.data_source.repository.TaskRepositoryImpl
import dev.sudhanshu.taskmanager.view.auth.AuthViewModel
import dev.sudhanshu.taskmanager.view.task.TaskViewModel
import dev.sudhanshu.taskmanager.utils.DatabaseUtils

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val databaseUtils = DatabaseUtils()
        val driverFactory = DatabaseDriverFactory(this)
        val database = databaseUtils.createDatabase(driverFactory.createDriver())

        // Initialize AuthRepository and AuthViewModel
        val authRepository = AuthRepository(database)
        val authViewModel = AuthViewModel(authRepository)
        val biometricAuthenticator = BiometricAuthenticator(this)

        val taskRepository = TaskRepositoryImpl(database)
        val taskViewModel  = TaskViewModel(taskRepository)

        val hapticFeedback = HapticFeedback(this)

        setContent {
            App(viewModel = authViewModel, biometricAuthenticator, taskViewModel, hapticFeedback)
        }
    }
}
