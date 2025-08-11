package com.amsterdam.viewmodel.sharedGame

data class TimerUiState(
    val currentTimerCount: Int = 0,
    val currentTimerColor: TimerColor = TimerColor.RED,
    val progress: Float = 0f
) {
    enum class TimerColor() {
        GREEN, RED
    }
}