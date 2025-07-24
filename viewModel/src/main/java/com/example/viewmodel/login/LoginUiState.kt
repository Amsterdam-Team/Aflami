package com.example.viewmodel.login

import com.example.domain.exceptions.AflamiException
import com.example.domain.exceptions.InvalidCredentialsException

data class LoginUiState(
    val username: String = "",
    val password: String = "",
    val isLoginButtonLoading: Boolean = false,
    val isLoginButtonEnabled: Boolean = false,
    val usernameError: UsernameErrorState? = null,
    val passwordError: PasswordErrorState? = null,
    val isPasswordShown: Boolean = false
)

sealed interface UsernameErrorState {
    data object InvalidCredentials: UsernameErrorState

    companion object{
        fun toUsernameErrorState(exception: AflamiException): UsernameErrorState?{
            return when (exception) {
                is InvalidCredentialsException -> InvalidCredentials
                else -> null
            }
        }
    }
}

sealed interface PasswordErrorState {
    data object InvalidCredentials: PasswordErrorState

    companion object{
        fun toPasswordErrorState(exception: AflamiException): PasswordErrorState?{
            return when (exception) {
                is InvalidCredentialsException -> InvalidCredentials
                else -> null
            }
        }
    }
}