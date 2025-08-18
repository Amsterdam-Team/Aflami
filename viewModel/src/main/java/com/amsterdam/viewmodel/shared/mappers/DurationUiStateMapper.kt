package com.amsterdam.viewmodel.shared.mappers

import com.amsterdam.viewmodel.shared.uiStates.DurationUiState

fun Int.toDurationUiState(): DurationUiState {
    return DurationUiState(
        hour = this / 60,
        minute = this % 60
    )
}