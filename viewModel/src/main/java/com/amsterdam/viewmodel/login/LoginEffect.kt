package com.amsterdam.viewmodel.login

sealed interface LoginEffect {
    data object NavigateToHome: LoginEffect
    data object ShowCredentialsError: LoginEffect
    data object NavigateToRegister: LoginEffect
    data object NavigateToResetPassword: LoginEffect
}