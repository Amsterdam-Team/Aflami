package com.amsterdam.ui.utils

import androidx.navigation.NavController

fun NavController.navigateUpWithFlag(flagName: String, value: Boolean) {
    this.previousBackStackEntry
        ?.savedStateHandle
        ?.set(flagName, value)

    this.navigateUp()
}