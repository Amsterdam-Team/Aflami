package com.amsterdam.ui.screens.gameResult

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.amsterdam.ui.navigation.Route

fun NavGraphBuilder.resultScreenRoute() {
    composable<Route.ResultScreen> {
        ResultScreen()
    }
}