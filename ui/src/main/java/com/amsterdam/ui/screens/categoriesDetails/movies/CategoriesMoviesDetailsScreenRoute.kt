package com.amsterdam.ui.screens.categoriesDetails.movies

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.amsterdam.ui.navigation.Route

fun NavGraphBuilder.categoriesMoviesDetailsScreenRoute(){
    composable<Route.CategoriesDetails> {
        CategoriesMoviesDetailsScreen()
    }

}