package com.amsterdam.ui.screens.myRating

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.amsterdam.ui.navigation.Route

internal fun NavGraphBuilder.myRatingScreenRoute(){
    composable<Route.MyRating> {
        MyRatingScreen()
    }
}