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