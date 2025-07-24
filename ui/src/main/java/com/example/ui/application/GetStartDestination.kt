package com.example.ui.application

import com.example.ui.navigation.Route
import com.example.viewmodel.application.ApplicationUiState

fun getStartDestination(startDestinations: ApplicationUiState.StartDestinations): Any{
    return when(startDestinations){
        ApplicationUiState.StartDestinations.HOME -> Route.Tab.Home
        ApplicationUiState.StartDestinations.LOGIN -> Route.Login
        ApplicationUiState.StartDestinations.ON_BOARDING -> Route.Login
    }
}