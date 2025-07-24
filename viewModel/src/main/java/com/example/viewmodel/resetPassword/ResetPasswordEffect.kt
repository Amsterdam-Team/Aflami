package com.example.viewmodel.resetPassword

sealed interface ResetPasswordEffect {
    data object NavigateToSignIn : ResetPasswordEffect
}