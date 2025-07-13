package com.amsterdam.ui.screens.categories

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.amsterdam.ui.navigation.Route

fun NavGraphBuilder.categoriesScreenRoute(){
    composable<Route.Tab.Categories> {
        CategoriesScreen()
    }
}