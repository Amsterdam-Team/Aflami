package com.amsterdam.ui.screens.search

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.amsterdam.ui.navigation.Route

fun NavGraphBuilder.searchScreenRoute(){
    composable<Route.Search> {
        SearchScreen()
    }
}