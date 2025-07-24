package com.example.viewmodel.application

import androidx.lifecycle.viewModelScope
import com.example.domain.useCase.authentication.GetsSessionType
import com.example.domain.utils.SessionType
import com.example.viewmodel.shared.BaseViewModel
import com.example.viewmodel.utils.dispatcher.DispatcherProvider
import kotlinx.coroutines.launch

class ApplicationViewModel(
    dispatcherProvider: DispatcherProvider,
    private val getsSessionType: GetsSessionType
): BaseViewModel<ApplicationUiState, Unit>(ApplicationUiState(), dispatcherProvider) {
    init {
        viewModelScope.launch {
            setStartDestination()
        }
    }

    private suspend fun setStartDestination(){
        val sessionType = getsSessionType()
        when(sessionType){
            SessionType.NOT_LOGGED_IN -> updateState { it.copy(startDestination = ApplicationUiState.StartDestinations.LOGIN) }
            SessionType.LOGGED_IN -> updateState { it.copy(startDestination = ApplicationUiState.StartDestinations.HOME) }
            SessionType.GUEST -> updateState { it.copy(startDestination = ApplicationUiState.StartDestinations.HOME) }
        }
    }
}