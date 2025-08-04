package com.amsterdam.viewmodel.onboarding

sealed interface OnboardingEffect {
    object NavigateToLoginScreen : OnboardingEffect
}