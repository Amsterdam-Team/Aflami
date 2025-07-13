package com.amsterdam.ui.screens.searchByActor

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.amsterdam.ui.navigation.Route

fun NavGraphBuilder.searchByActorScreenRoute(){
    composable<Route.SearchByActor> {
        SearchByActorScreen()
    }
}