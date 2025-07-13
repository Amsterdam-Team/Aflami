package com.amsterdam.ui.screens.searchByCountry

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.amsterdam.ui.navigation.Route

fun NavGraphBuilder.searchByCountryScreenRoute(){
    composable<Route.SearchByCountry> {
        SearchByCountryScreen()
    }
}