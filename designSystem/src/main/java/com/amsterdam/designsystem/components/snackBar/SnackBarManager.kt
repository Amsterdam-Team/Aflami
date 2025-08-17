package com.amsterdam.designsystem.components.snackBar

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

object SnackBarManager {
    private val _snackBarFlow = MutableSharedFlow<SnackBarData>(
        extraBufferCapacity = 1
    )
    val snackBarFlow: SharedFlow<SnackBarData> = _snackBarFlow.asSharedFlow()

    private fun show(
        message: String,
        status: SnackBarStatus,
        duration: Long = 3000L
    ) {
        if (message.isEmpty()) return
        val snackBarData = SnackBarData(message, status, duration)
        _snackBarFlow.tryEmit(snackBarData)
    }

    fun showSuccess(message: String, duration: Long = 3000L) {
        show(message, SnackBarStatus.Success, duration)
    }

    fun showError(message: String, duration: Long = 3000L) {
        show(message, SnackBarStatus.Failure, duration)
    }
}
