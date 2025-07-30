package com.amsterdam.viewmodel.application

import androidx.lifecycle.viewModelScope
import com.amsterdam.domain.useCase.authentication.GetsSessionType
import com.amsterdam.domain.utils.SessionType
import com.amsterdam.viewmodel.shared.BaseViewModel
import com.amsterdam.viewmodel.utils.dispatcher.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ApplicationViewModel @Inject constructor(
    dispatcherProvider: DispatcherProvider,
    private val getsSessionType: GetsSessionType
): BaseViewModel<ApplicationUiState, Unit>(ApplicationUiState(), dispatcherProvider) {

    suspend fun setStartDestination(): ApplicationUiState.StartDestinations{
        val sessionType = getsSessionType()
        return when(sessionType){
            SessionType.NOT_LOGGED_IN -> ApplicationUiState.StartDestinations.LOGIN
            SessionType.LOGGED_IN ->  ApplicationUiState.StartDestinations.HOME
            SessionType.GUEST ->  ApplicationUiState.StartDestinations.HOME
        }
    }
}