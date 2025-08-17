package com.amsterdam.ui.components.bottomNavigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.amsterdam.ui.R
import com.amsterdam.ui.navigation.Route

enum class BottomBarItems(
    @DrawableRes val icon: Int,
    @StringRes val label: Int,
    val route: Route
) {
    HOME(
        icon = com.amsterdam.designsystem.R.drawable.ic_nav_home,
        label = R.string.home,
        route = Route.Tab.Home
    ),
    LISTS(
        icon = com.amsterdam.designsystem.R.drawable.ic_nav_lists,
        label = R.string.lists,
        route = Route.Tab.Lists
    ),
    CATEGORIES(
        icon = com.amsterdam.designsystem.R.drawable.ic_nav_categories,
        label = R.string.categories,
        route = Route.Tab.Categories
    ),
    LETS_PLAY(
        icon = com.amsterdam.designsystem.R.drawable.ic_nav_games,
        label = R.string.lets_play,
        route = Route.Tab.LetsPlay
    ),
    PROFILE(
        icon = com.amsterdam.designsystem.R.drawable.ic_nav_profile,
        label = R.string.profile,
        route = Route.Tab.Profile
    ),
}