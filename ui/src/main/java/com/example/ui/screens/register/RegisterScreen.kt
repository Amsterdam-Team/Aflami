package com.example.ui.screens.register

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.designsystem.components.LoadingContainer
import com.example.designsystem.theme.AppTheme
import com.example.ui.application.LocalNavController
import com.example.ui.screens.home.sections.AnimatedSectionVisibility
import com.example.ui.screens.register.components.RegisterWebView
import com.example.viewmodel.register.RegisterEffect
import com.example.viewmodel.register.RegisterUiState
import com.example.viewmodel.register.RegisterViewModel
import org.koin.androidx.compose.koinViewModel


@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel = koinViewModel()
) {
    val navController = LocalNavController.current
    val state by viewModel.state.collectAsState()


    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                RegisterEffect.NavigateToSignIn -> navController.popBackStack()
                else -> {}
            }
        }
    }

    RegisterContent(
        state = state,
        onLoadingChanged = viewModel::setLoading,
        onRegistrationComplete = viewModel::onRegistrationComplete
    )
}

@SuppressLint("JavascriptInterface")
@Composable
private fun RegisterContent(
    state: RegisterUiState,
    onLoadingChanged: (Boolean) -> Unit,
    onRegistrationComplete: () -> Unit
) {

    Box(
        Modifier
            .fillMaxSize()
            .background(AppTheme.color.surface)
            .statusBarsPadding()
            .navigationBarsPadding(),
        contentAlignment = Alignment.Center
    ) {
        RegisterWebView(
            modifier = Modifier.fillMaxSize(),
            onLoadingStateChanged = onLoadingChanged,
            urlToLoad = state.signUpUrl,
            onRegistrationComplete = onRegistrationComplete
        )

        AnimatedSectionVisibility(state.isLoading) {
            LoadingContainer()
        }

    }
}
