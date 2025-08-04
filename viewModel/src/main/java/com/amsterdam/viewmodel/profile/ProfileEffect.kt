package com.amsterdam.viewmodel.profile

sealed interface ProfileEffect {
    data object NavigateToLogin : ProfileEffect
}