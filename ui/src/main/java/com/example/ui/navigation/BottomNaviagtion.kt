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
import com.example.designsystem.components.bottomNavBar.BottomBarItems
import com.example.designsystem.components.bottomNavBar.BottomNavBar

private val navigationBarItems = mapOf(
    BottomBarItems.HOME to Route.Tab.Home,
    BottomBarItems.LISTS to Route.Tab.Lists,
    BottomBarItems.CATEGORIES to Route.Tab.Categories,
    BottomBarItems.LETS_PLAY to Route.Tab.LetsPlay,
    BottomBarItems.PROFILE to Route.Tab.Profile
)

@Composable
fun BottomNavigation(
    currentDestination: NavDestination?,
    onNavigate: (Any) -> Unit,
    modifier: Modifier = Modifier
) {
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
            selectedBottomBarItems = selectedDestination,
            onDestinationClicked = { onNavigate(it) }
        )
    }
}

private fun getSelectedDestination(
    currentDestination: NavDestination?,
): BottomBarItems {
    return navigationBarItems.entries.find { (_, route) ->
        currentDestination?.hierarchy?.any { navDestination ->
            navDestination.hasRoute(route::class)
        } == true
    }?.key ?: BottomBarItems.PROFILE
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