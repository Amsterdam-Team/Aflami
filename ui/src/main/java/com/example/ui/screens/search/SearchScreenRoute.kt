package com.example.ui.screens.search

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.ui.navigation.Route

internal fun NavGraphBuilder.searchScreenRoute(){
    composable<Route.Search> {
        SearchScreen()
    }
}