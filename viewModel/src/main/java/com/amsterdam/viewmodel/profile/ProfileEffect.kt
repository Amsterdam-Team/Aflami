package com.amsterdam.viewmodel.profile

sealed interface ProfileEffect {
    data object NavigateToLogin : ProfileEffect
    data object NavigateToResetPassword: ProfileEffect
    data object NavigateToMyRating: ProfileEffect
    data object ShowError: ProfileEffect
}