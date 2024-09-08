package dev.sudhanshu.taskmanager.view.home

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dev.sudhanshu.taskmanager.HapticFeedback
import dev.sudhanshu.taskmanager.view.auth.AuthViewModel
import dev.sudhanshu.taskmanager.utils.bottomNavigationItemsList
import org.jetbrains.compose.resources.stringResource
import dev.sudhanshu.taskmanager.view.navigation.NewsBottomNavigation
import dev.sudhanshu.taskmanager.view.navigation.graphs.MainNavGraph
import dev.sudhanshu.taskmanager.view.task.TaskViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: AuthViewModel,
    mainNavController: NavHostController,
    rootNavController: NavHostController,
    taskViewModel: TaskViewModel,
    hapticFeedback: HapticFeedback
) {
    val homeNavController = rememberNavController()
    val navBackStackEntry by homeNavController.currentBackStackEntryAsState()

    // Getting the current route safely
    val currentRoute by remember(navBackStackEntry) {
        derivedStateOf {
            navBackStackEntry?.destination?.route
        }
    }

    // Default to the first item's title if currentRoute is null or not found
    val topBarTitle by remember(currentRoute) {
        derivedStateOf {
            currentRoute?.let { route ->
                bottomNavigationItemsList.firstOrNull { it.route == route }?.title
            } ?: bottomNavigationItemsList.first().title
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(topBarTitle),
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            )
        },
        bottomBar = {
            NewsBottomNavigation(
                items = bottomNavigationItemsList,
                currentRoute = currentRoute,
                onItemClick = { currentNavigationItem ->
                    homeNavController.navigate(currentNavigationItem.route) {
                        homeNavController.graph.startDestinationRoute?.let { startDestinationRoute ->
                            popUpTo(startDestinationRoute) {
                                saveState = true
                            }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    ) { innerPadding ->
        MainNavGraph(
            homeNavController = homeNavController,
            innerPadding = innerPadding,
            viewModel = viewModel,
            rootNavController = rootNavController,
            taskViewModel = taskViewModel,
            hapticFeedback = hapticFeedback
        )
    }
}
