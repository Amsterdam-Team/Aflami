package com.amsterdam.viewmodel.application

import com.amsterdam.domain.utils.RestrictionLevel
import com.amsterdam.domain.useCase.preferences.ManageLocaleLanguageUseCase

data class ApplicationUiState(
    val startDestination: StartDestinations = StartDestinations.LOGIN,
    val restrictionLevel: RestrictionLevel = RestrictionLevel.STRICT,
    val isDarkTheme: Boolean = true,
    val isThemeLoaded: Boolean = false,
    val language: ManageLocaleLanguageUseCase.Language = ManageLocaleLanguageUseCase.Language.ENGLISH
){
    enum class StartDestinations{
        HOME,
        LOGIN,
        ON_BOARDING
    }
}
