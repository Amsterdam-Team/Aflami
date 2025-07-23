package com.example.ui.screens.register


import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.ui.navigation.Route

fun NavGraphBuilder.registerScreenRoute(){
    composable<Route.Register> {
        RegisterScreen()
    }
}
