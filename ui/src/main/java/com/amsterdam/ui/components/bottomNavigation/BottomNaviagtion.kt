package com.amsterdam.ui.components.bottomNavigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import com.amsterdam.designsystem.components.bottomNavBar.NavigationBar
import com.amsterdam.ui.navigation.Route


@Composable
fun BottomNavigation(
    currentDestination: NavDestination?,
    onNavigate: (Route) -> Unit,
    modifier: Modifier = Modifier,
) {
    val visible =
        rememberSaveable(currentDestination) {
            shouldShowBottomNavigation(currentDestination)
        }

    val selectedDestination =
        remember(currentDestination) {
            getSelectedDestination(currentDestination)
        }

    AnimatedVisibility(
        visible = visible,
        enter =
            slideInVertically(
                animationSpec = tween(600),
                initialOffsetY = { it },
            ),
        exit =
            slideOutVertically(
                animationSpec = tween(600),
                targetOffsetY = { it },
            ),
    ) {
        BottomNavBar(
            modifier = modifier,
            items = BottomBarItems.entries,
            selectedBottomBarItems = selectedDestination,
            onDestinationClicked = { onNavigate(it) },
        )
    }
}

private fun getSelectedDestination(currentDestination: NavDestination?): BottomBarItems =
    BottomBarItems.entries
        .find { entry ->
            currentDestination?.hierarchy?.any { navDestination ->
                navDestination.hasRoute(entry.route::class)
            } == true
        } ?: BottomBarItems.PROFILE

private fun shouldShowBottomNavigation(currentDestination: NavDestination?): Boolean =
    BottomBarItems.entries
        .map { it.route::class }
        .any { route ->
            currentDestination?.hierarchy?.any { destination ->
                destination.hasRoute(route)
            } == true
        }
