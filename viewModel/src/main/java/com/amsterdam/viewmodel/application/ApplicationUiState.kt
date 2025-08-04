package com.amsterdam.viewmodel.application

import com.amsterdam.domain.utils.RestrictionLevel

data class ApplicationUiState(
    val startDestination: StartDestinations = StartDestinations.LOGIN,
    val restrictionLevel: RestrictionLevel = RestrictionLevel.STRICT
){
    enum class StartDestinations{
        HOME,
        LOGIN,
        ON_BOARDING
    }
}
