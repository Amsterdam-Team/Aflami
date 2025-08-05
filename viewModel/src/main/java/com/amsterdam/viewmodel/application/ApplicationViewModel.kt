package com.amsterdam.viewmodel.application

import android.content.Context
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
        viewModelScope.launch {
            getRestrictionLevel()
        }
    }

    suspend fun setStartDestination(): ApplicationUiState.StartDestinations {
        val isOnboardingCompleted = getOnboardingStatusUseCase()

        if (!isOnboardingCompleted) {
            return ApplicationUiState.StartDestinations.ON_BOARDING
        }

        val sessionType = getsSessionType()
        return when (sessionType) {
            SessionType.NOT_LOGGED_IN -> ApplicationUiState.StartDestinations.LOGIN
            SessionType.LOGGED_IN -> ApplicationUiState.StartDestinations.HOME
            SessionType.GUEST -> ApplicationUiState.StartDestinations.HOME
        }
    }

    fun initAppSettings(locale: Locale) {
        viewModelScope.launch(dispatcherProvider.IO) {
            manageLocaleLanguageUseCase.initAppLanguage(locale.language)
        }
    }

    private fun listenToAppSettings() {
        viewModelScope.launch(dispatcherProvider.IO) {
            manageAppThemeUseCase.getAppTheme().collect { isDarkTheme ->
                updateState { state ->
                    state.copy(
                        isDarkTheme = isDarkTheme
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

    private suspend fun getRestrictionLevel(){
        val restrictionLevel = manageRestrictionLevelUseCase.getRestrictionLevel()
        restrictionLevel.collectLatest { restriction ->
            updateState {
                it.copy(restrictionLevel = restriction)
            }
        }
    }
}