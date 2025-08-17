package com.amsterdam.viewmodel.application

import androidx.lifecycle.viewModelScope
import com.amsterdam.domain.useCase.authentication.GetsSessionType
import com.amsterdam.domain.useCase.preferences.GetOnboardingStatusUseCase
import com.amsterdam.domain.useCase.preferences.ManageAppThemeUseCase
import com.amsterdam.domain.useCase.preferences.ManageLocaleLanguageUseCase
import com.amsterdam.domain.useCase.preferences.ManageRestrictionLevelUseCase
import com.amsterdam.domain.utils.SessionType
import com.amsterdam.viewmodel.shared.BaseViewModel
import com.amsterdam.viewmodel.utils.dispatcher.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class ApplicationViewModel @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
    private val manageLocaleLanguageUseCase: ManageLocaleLanguageUseCase,
    private val getOnboardingStatusUseCase: GetOnboardingStatusUseCase,
    private val manageAppThemeUseCase: ManageAppThemeUseCase,
    private val getsSessionType: GetsSessionType,
    private val manageRestrictionLevelUseCase: ManageRestrictionLevelUseCase
) : BaseViewModel<ApplicationUiState, Unit>(ApplicationUiState(), dispatcherProvider) {

    init {
        listenToAppSettings()
        setStartDestination()
        getRestrictionLevel()
    }

    private fun setStartDestination() {
        viewModelScope.launch(dispatcherProvider.IO) {
            val isOnboardingCompleted = getOnboardingStatusUseCase()

            if (!isOnboardingCompleted) {
                updateState {
                    it.copy(
                        startDestination = ApplicationUiState.StartDestinations.ON_BOARDING,
                        isDestinationLoaded = true
                    )
                }
            } else {
                setNonOnboardingStartDestination()
            }
        }
    }

    fun initAppSettings(locale: Locale) {
        viewModelScope.launch(dispatcherProvider.IO) {
            manageLocaleLanguageUseCase.initAppLanguage(locale.language)
        }
    }

    private suspend fun setNonOnboardingStartDestination(){
        val sessionType = getsSessionType()
        val destination = when (sessionType) {
            SessionType.LOGGED_IN -> ApplicationUiState.StartDestinations.HOME
            SessionType.GUEST -> ApplicationUiState.StartDestinations.HOME
            null -> ApplicationUiState.StartDestinations.LOGIN
        }
        updateState {
            it.copy(
                startDestination = destination,
                isDestinationLoaded = true
            )
        }
    }

    private fun listenToAppSettings() {
        viewModelScope.launch(dispatcherProvider.IO) {
            manageAppThemeUseCase.getAppTheme().collect { isDarkTheme ->

                updateState { state ->
                    state.copy(
                        isDarkTheme = isDarkTheme,
                        isThemeLoaded = true
                    )
                }
            }
        }

        viewModelScope.launch(dispatcherProvider.IO) {
            manageLocaleLanguageUseCase.getAppLanguage().collect { language ->
                updateState { state ->
                    state.copy(
                        language = language
                    )
                }
            }
        }
    }

    private fun getRestrictionLevel() {
        viewModelScope.launch {
            val restrictionLevel = manageRestrictionLevelUseCase.getRestrictionLevel()
            restrictionLevel.collectLatest { restriction ->
                updateState {
                    it.copy(restrictionLevel = restriction)
                }
            }
        }
    }
}