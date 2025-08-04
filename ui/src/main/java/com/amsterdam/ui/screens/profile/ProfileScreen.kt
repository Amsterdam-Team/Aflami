package com.amsterdam.ui.screens.profile

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.amsterdam.designsystem.components.LoadingContainer
import com.amsterdam.designsystem.theme.AppTheme
import com.amsterdam.designsystem.utils.ThemeAndLocalePreviews
import com.amsterdam.ui.application.LocalNavController
import com.amsterdam.ui.application.LocalScaffoldBottomPadding
import com.amsterdam.ui.navigation.Route
import com.amsterdam.ui.screens.profile.components.LoggedInContent
import com.amsterdam.ui.screens.profile.components.NotLoggedInContent
import com.amsterdam.viewmodel.profile.ProfileEffect
import com.amsterdam.viewmodel.profile.ProfileInteractionListener
import com.amsterdam.viewmodel.profile.ProfileUiState
import com.amsterdam.viewmodel.profile.ProfileViewModel

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val navController = LocalNavController.current

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                ProfileEffect.NavigateToLogin -> navController.navigate(Route.Login)
            }
        }
    }
    ProfileScreenContent(
        state,
        interactionListener = viewModel
    )
}

@Composable
private fun ProfileScreenContent(
    state: ProfileUiState,
    interactionListener: ProfileInteractionListener
) {
    val animationDuration by remember { mutableIntStateOf(1000) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.color.surface)
            .navigationBarsPadding()
            .windowInsetsPadding(WindowInsets(bottom = LocalScaffoldBottomPadding.current)),
        contentAlignment = Alignment.TopStart
    ) {
        AnimatedVisibility(
            state.isLoading,
            enter = fadeIn(tween(animationDuration)),
            exit = fadeOut(tween(animationDuration)),
        ) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                LoadingContainer()
            }
        }

        AnimatedVisibility(
            state.isUserLoggedIn && !state.isLoading,
            enter = fadeIn(tween(animationDuration)),
            exit = fadeOut(tween(animationDuration)),
        ) {
            LoggedInContent()
        }

        AnimatedVisibility(
            !state.isUserLoggedIn && !state.isLoading,
            enter = fadeIn(tween(animationDuration)),
            exit = fadeOut(tween(animationDuration)),
        ) {
            NotLoggedInContent(
                interactionListener::onClickLogin
            )
        }
    }
}

@ThemeAndLocalePreviews
@Composable
private fun ProfileScreenPreview() {
    ProfileScreenContent(
        ProfileUiState(),
        interactionListener = object : ProfileInteractionListener {
            override fun onClickLogin() {}
        }
    )
}