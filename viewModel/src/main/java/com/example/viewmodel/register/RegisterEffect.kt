package com.example.viewmodel.register

sealed interface RegisterEffect {
    data object NavigateToSignIn : RegisterEffect
}