package com.example.ui.screens.search.actorSearch

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.ui.navigation.Route

fun NavGraphBuilder.searchByActorScreenRoute(){
    composable<Route.SearchByActor> {
        SearchByActorScreen()
    }
}