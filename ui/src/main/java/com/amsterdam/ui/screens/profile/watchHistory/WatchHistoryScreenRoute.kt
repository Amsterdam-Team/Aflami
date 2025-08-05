package com.amsterdam.ui.screens.profile.watchHistory

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.amsterdam.ui.navigation.Route

fun NavGraphBuilder.watchHistoryScreenRoute() {
    composable<Route.WatchHistory> {
        WatchHistoryScreen()
    }
}