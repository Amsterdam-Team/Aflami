package com.amsterdam.viewmodel.onboarding

data class OnboardingUiState(
    val currentPageIndex: Int = 0,
    val totalPages: Int = 4,
    val isLastPage: Boolean = false
)