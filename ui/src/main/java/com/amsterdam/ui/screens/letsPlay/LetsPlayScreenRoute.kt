package com.amsterdam.ui.screens.letsPlay

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.amsterdam.ui.navigation.Route

fun NavGraphBuilder.letsPlayScreenRoute(){
    composable<Route.Tab.LetsPlay> {
        LetsPlayScreen()
    }
}