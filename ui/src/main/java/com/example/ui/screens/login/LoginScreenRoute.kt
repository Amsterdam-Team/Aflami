package com.example.ui.screens.login

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.ui.navigation.Route
import com.example.ui.screens.lists.ListsScreen

fun NavGraphBuilder.loginScreenRoute(){
    composable<Route.Login> {
        LoginScreen()
    }
}