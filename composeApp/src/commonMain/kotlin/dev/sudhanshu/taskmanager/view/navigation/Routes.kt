package ui.navigation


object Graph {
    const val RootGraph = "rootScreenGraph"
    const val MainScreenGraph = "mainScreenGraph"
}

sealed class MainRouteScreen(var route: String) {

    object home : MainRouteScreen("home")
    object dashboard : MainRouteScreen("dashboard")
    object profile : MainRouteScreen("profile")
    object addEditScreen : MainRouteScreen("taskAddEdit")
}

