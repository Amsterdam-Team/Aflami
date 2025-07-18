package com.example.ui.screens.seriesDetails

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.ui.navigation.Route

fun NavGraphBuilder.seriesDetailsScreenRoute() {
    composable<Route.SeriesDetails> {
        SeriesDetailsScreen()
    }
}