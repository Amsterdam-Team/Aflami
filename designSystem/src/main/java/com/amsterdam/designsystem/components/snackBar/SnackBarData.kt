package com.amsterdam.designsystem.components.snackBar

data class SnackBarData(
    val message: String,
    val status: SnackBarStatus,
    val duration: Long = 3000L)