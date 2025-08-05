package com.amsterdam.viewmodel.shared

data class RateDialogUiState(
    val selectedStarIndex: Int? = null,
    val previousStarIndex: Int? = null,
    val isVisible: Boolean = false,
    val isSubmittingEnabled: Boolean = false,
    val isLoading: Boolean = false
)