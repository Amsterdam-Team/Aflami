package com.amsterdam.ui.screens.categoriesDetails.tvShow

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.amsterdam.ui.navigation.Route

fun NavGraphBuilder.categoriesTvShowsDetailsScreen(){
    composable<Route.CategoriesTvShowsDetails>{
        CategoriesTvShowsDetailsScreen()

    }
}