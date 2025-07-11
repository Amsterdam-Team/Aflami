package com.example.ui.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import com.example.designsystem.components.bottomNavBar.BottomNavBar
import com.example.designsystem.components.bottomNavBar.Destination

private val navigationBarItems = mapOf(
    Destination.HOME to Route.Tab.Home,
    Destination.LISTS to Route.Tab.Lists,
    Destination.CATEGORIES to Route.Tab.Categories,
    Destination.LETS_PLAY to Route.Tab.LetsPlay,
    Destination.PROFILE to Route.Tab.Profile
)

@Composable
fun BottomNavigation(
    currentDestination: NavDestination?,
    onNavigate: (Any) -> Unit,
    modifier: Modifier = Modifier
){
    val visible = remember(currentDestination) {
        shouldShowBottomNavigation(currentDestination)
    }

    val selectedDestination = remember(currentDestination) {
        getSelectedDestination(currentDestination)
    }

    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically(
            animationSpec = tween(600),
            initialOffsetY = { it }
        ),
        exit = slideOutVertically(
            animationSpec = tween(600),
            targetOffsetY = { it }
        )
    ) {
        BottomNavBar(
            modifier = modifier,
            items = navigationBarItems,
            selectedDestination = selectedDestination,
            onDestinationClicked = { onNavigate(it) }
        )
    }
}

private fun getSelectedDestination(
    currentDestination: NavDestination?,
): Destination {
    return navigationBarItems.entries.find { (_, route) ->
        currentDestination?.hierarchy?.any { navDestination ->
            navDestination.hasRoute(route::class)
        } == true
    }?.key ?: Destination.PROFILE
}

private fun shouldShowBottomNavigation(currentDestination: NavDestination?): Boolean {
    return navigationBarItems.entries
        .map { it.value::class }
        .any { route ->
            currentDestination?.hierarchy?.any { destination ->
                destination.hasRoute(route)
            } == true
        }
}