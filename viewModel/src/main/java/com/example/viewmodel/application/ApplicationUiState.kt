package com.example.viewmodel.application

data class ApplicationUiState(
    val startDestination: StartDestinations = StartDestinations.LOGIN
){
    enum class StartDestinations{
        HOME,
        LOGIN,
        ON_BOARDING
    }
}
