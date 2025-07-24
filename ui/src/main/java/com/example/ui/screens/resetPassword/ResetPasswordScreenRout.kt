package com.example.ui.screens.resetPassword


import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.ui.navigation.Route
import com.example.ui.screens.ResetPassword.ResetPasswordScreen

fun NavGraphBuilder.resetPasswordScreenRoute() {
    composable<Route.ResetPassword> {
        ResetPasswordScreen()
    }
}
