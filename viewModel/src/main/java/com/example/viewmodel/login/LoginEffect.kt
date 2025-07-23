package com.example.viewmodel.login

sealed interface LoginEffect {
    data object NavigateToHome: LoginEffect
    data object NavigateToRegister: LoginEffect
}