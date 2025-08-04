package com.amsterdam.ui.screens.listDetails

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.amsterdam.ui.navigation.Route

fun NavGraphBuilder.listDetailsScreenRoute(){
    composable<Route.ListDetails> {
        ListsDetailsScreen()
    }
}