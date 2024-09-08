package dev.sudhanshu.taskmanager.view.navigation.graphs

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import dev.sudhanshu.taskmanager.HapticFeedback
import dev.sudhanshu.taskmanager.view.addedittask.AddEditTaskScreen
import dev.sudhanshu.taskmanager.view.auth.AuthViewModel
import dev.sudhanshu.taskmanager.view.dashboard.DashboardScreen
import ui.navigation.Graph
import dev.sudhanshu.taskmanager.view.task.TaskScreen
import dev.sudhanshu.taskmanager.view.profile.ProfileScreen
import dev.sudhanshu.taskmanager.view.task.TaskViewModel
import dev.sudhanshu.taskmanager.utils.FadeIn
import dev.sudhanshu.taskmanager.utils.FadeOut
import ui.navigation.MainRouteScreen


@Composable
fun MainNavGraph(
    homeNavController: NavHostController,
    innerPadding: PaddingValues,
    viewModel: AuthViewModel,
    rootNavController: NavHostController,
    taskViewModel: TaskViewModel,
    hapticFeedback: HapticFeedback,
) {
    NavHost(
        modifier = Modifier.fillMaxSize().padding(innerPadding),
        navController = homeNavController,
        route = Graph.MainScreenGraph,
        startDestination = MainRouteScreen.home.route,
        enterTransition = { FadeIn },
        exitTransition = { FadeOut },
    ) {

        composable(route = MainRouteScreen.home.route) {
            TaskScreen(taskViewModel, homeNavController, hapticFeedback)
        }

        composable(route = MainRouteScreen.dashboard.route) {
            DashboardScreen(taskViewModel)
        }

        composable(route = MainRouteScreen.profile.route) {
            ProfileScreen(viewModel, rootNavController, taskViewModel)
        }


        composable(
            route = MainRouteScreen.addEditScreen.route
        ) {
            AddEditTaskScreen(taskViewModel, homeNavController, hapticFeedback)
        }
    }

}