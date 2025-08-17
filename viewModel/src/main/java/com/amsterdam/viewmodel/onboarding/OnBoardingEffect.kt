package com.amsterdam.viewmodel.onboarding

sealed interface OnboardingEffect {
    data object NavigateToLoginScreen : OnboardingEffect
}