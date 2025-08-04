package com.amsterdam.ui.screens.onBoarding

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.amsterdam.ui.navigation.Route

fun NavGraphBuilder.onboardingScreenRoute() {
    composable<Route.Onboarding> {
        OnboardingScreen()
    }
}