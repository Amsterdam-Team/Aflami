package com.example.designsystem.components.bottomNavBar

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.designsystem.R

enum class BottomBarItems(
    @DrawableRes val icon: Int,
    @StringRes val label: Int
) {
    HOME(
        icon = R.drawable.ic_nav_home,
        label = R.string.home
    ),
    LISTS(
        icon = R.drawable.ic_nav_lists,
        label = R.string.lists,
    ),
    CATEGORIES(
        icon = R.drawable.ic_nav_categories,
        label = R.string.categories,
    ),
    LETS_PLAY(
        icon = R.drawable.ic_nav_games,
        label = R.string.lets_play,
    ),
    PROFILE(
        icon = R.drawable.ic_nav_profile,
        label = R.string.profile,
    )
}