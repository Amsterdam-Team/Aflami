package com.example.ui.screens.search.countrySearch

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.ui.navigation.Route

fun NavGraphBuilder.searchByCountryScreenRoute(){
    composable<Route.SearchByCountry> {
        SearchByCountryScreen()
    }
}