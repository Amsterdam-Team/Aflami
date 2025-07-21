package com.example.viewmodel.login

sealed interface LoginScreenEffect {
    data object NavigateToHome: LoginScreenEffect
}