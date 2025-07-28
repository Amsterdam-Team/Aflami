package com.amsterdam.viewmodel.register

import com.amsterdam.viewmodel.BuildConfig
import com.amsterdam.viewmodel.shared.BaseViewModel
import com.amsterdam.viewmodel.utils.dispatcher.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(dispatcherProvider: DispatcherProvider) :
    BaseViewModel<RegisterUiState, RegisterEffect>(RegisterUiState(), dispatcherProvider) {
    init {
        updateState { it.copy(signUpUrl = BuildConfig.MOVIE_SIGN_UP_URL) }

    }

    fun setLoading(isLoading: Boolean) {
        updateState { it.copy(isLoading = isLoading) }
    }

    fun onRegistrationComplete() {
        sendNewEffect(RegisterEffect.NavigateToSignIn)
    }
}