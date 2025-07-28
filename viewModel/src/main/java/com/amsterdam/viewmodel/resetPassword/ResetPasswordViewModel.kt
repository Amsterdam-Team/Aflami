package com.amsterdam.viewmodel.resetPassword

import com.amsterdam.viewmodel.BuildConfig
import com.amsterdam.viewmodel.shared.BaseViewModel
import com.amsterdam.viewmodel.utils.dispatcher.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ResetPasswordViewModel @Inject constructor(dispatcherProvider: DispatcherProvider) :
    BaseViewModel<ResetPasswordUiState, ResetPasswordEffect>(
        ResetPasswordUiState(),
        dispatcherProvider
    ) {


    init {

        //updateState { it.copy(resetPasswordUrl = BuildConfig.MOVIE_RESET_PASSWORD_URL) }
    }

    fun setLoading(isLoading: Boolean) {
        updateState { it.copy(isLoading = isLoading) }
    }

    fun onResetPasswordComplete() {
        sendNewEffect(ResetPasswordEffect.NavigateToSignIn)
    }
}