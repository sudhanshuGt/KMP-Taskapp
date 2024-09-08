package dev.sudhanshu.taskmanager.view.navigation.graphs

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dev.sudhanshu.taskmanager.HapticFeedback
import dev.sudhanshu.taskmanager.view.auth.AuthViewModel
import dev.sudhanshu.taskmanager.view.home.MainScreen
import dev.sudhanshu.taskmanager.view.task.TaskViewModel
import ui.navigation.Graph

// defining root navigation of app
@Composable
fun RootNavGraph(
    viewModel: AuthViewModel,
    rootNav: NavHostController,
    taskViewModel: TaskViewModel,
    hapticFeedback: HapticFeedback
) {
    val homeNavController = rememberNavController()
    NavHost(
        navController = homeNavController,
        route = Graph.RootGraph,
        startDestination = Graph.MainScreenGraph,
    ) {
        composable(route = Graph.MainScreenGraph){
            MainScreen(viewModel,homeNavController, rootNav, taskViewModel, hapticFeedback)
        }
    }
}