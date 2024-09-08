package dev.sudhanshu.taskmanager.utils

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import org.jetbrains.compose.resources.StringResource
import taskmanager.composeapp.generated.resources.Res
import taskmanager.composeapp.generated.resources.dark_mode
import taskmanager.composeapp.generated.resources.dashboard
import taskmanager.composeapp.generated.resources.ic_dashboard
import taskmanager.composeapp.generated.resources.ic_home
import taskmanager.composeapp.generated.resources.ic_person
import taskmanager.composeapp.generated.resources.light_mode
import taskmanager.composeapp.generated.resources.profile
import taskmanager.composeapp.generated.resources.system_default
import taskmanager.composeapp.generated.resources.task
import dev.sudhanshu.taskmanager.view.navigation.BottomNavigationItem
import ui.navigation.MainRouteScreen


val bottomNavigationItemsList = listOf(
    BottomNavigationItem(
        icon = Res.drawable.ic_home,
        title = Res.string.task,
        route = MainRouteScreen.home.route,
    ),
    BottomNavigationItem(
        icon = Res.drawable.ic_dashboard,
        title = Res.string.dashboard,
        route = MainRouteScreen.dashboard.route,
    ),
    BottomNavigationItem(
        icon = Res.drawable.ic_person,
        title = Res.string.profile,
        route = MainRouteScreen.profile.route,
    ),
)
enum class Theme(val title: StringResource) {
    SYSTEM_DEFAULT(Res.string.system_default),
    LIGHT_MODE(Res.string.light_mode),
    DARK_MODE(Res.string.dark_mode)
}
enum class Type {
   Mobile, Desktop
}


val FadeIn = fadeIn(animationSpec = tween(220, delayMillis = 90)) +
        scaleIn(
            initialScale = 0.92f,
            animationSpec = tween(220, delayMillis = 90)
        )

val FadeOut = fadeOut(animationSpec = tween(90))