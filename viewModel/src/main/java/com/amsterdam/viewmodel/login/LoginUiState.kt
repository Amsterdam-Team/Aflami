package com.amsterdam.viewmodel.login

import com.amsterdam.domain.exceptions.AccountDisabledException
import com.amsterdam.domain.exceptions.AflamiException
import com.amsterdam.domain.exceptions.InvalidCredentialsException
import com.amsterdam.domain.exceptions.NetworkException
import com.amsterdam.domain.exceptions.VerificationRequiredException

data class LoginUiState(
    val username: String = "",
    val password: String = "",
    val isLoginButtonLoading: Boolean = false,
    val isLoginButtonEnabled: Boolean = false,
    val loginError: LoginErrorState? = null,
    val isPasswordShown: Boolean = false
)

sealed interface LoginErrorState {
    data object InvalidCredentials: LoginErrorState
    data object VerificationRequired: LoginErrorState
    data object AccountDisabled: LoginErrorState
    data object NoInternet: LoginErrorState
    data object UnknownError: LoginErrorState

    companion object{
        fun toLoginErrorState(exception: AflamiException): LoginErrorState{
            return when (exception) {
                is InvalidCredentialsException -> InvalidCredentials
                is VerificationRequiredException -> VerificationRequired
                is AccountDisabledException -> AccountDisabled
                is NetworkException -> NoInternet
                else -> UnknownError
            }
        }
    }
}