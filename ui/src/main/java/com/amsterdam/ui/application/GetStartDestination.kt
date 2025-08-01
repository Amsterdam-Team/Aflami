package com.amsterdam.ui.application

import com.amsterdam.ui.navigation.Route
import com.amsterdam.viewmodel.application.ApplicationUiState

fun getStartDestination(startDestinations: ApplicationUiState.StartDestinations): Route{
    return when(startDestinations){
        ApplicationUiState.StartDestinations.HOME -> Route.Tab.Home
        ApplicationUiState.StartDestinations.LOGIN -> Route.Login
        ApplicationUiState.StartDestinations.ON_BOARDING -> Route.Login
    }
}