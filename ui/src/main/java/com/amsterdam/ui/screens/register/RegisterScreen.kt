package com.amsterdam.ui.screens.register

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
import androidx.hilt.navigation.compose.hiltViewModel
import com.amsterdam.designsystem.components.LoadingContainer
import com.amsterdam.designsystem.theme.AppTheme
import com.amsterdam.ui.application.LocalNavManager
import com.amsterdam.ui.screens.home.sections.AnimatedSectionVisibility
import com.amsterdam.ui.screens.register.components.RegisterWebView
import com.amsterdam.viewmodel.register.RegisterEffect
import com.amsterdam.viewmodel.register.RegisterUiState
import com.amsterdam.viewmodel.register.RegisterViewModel

@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel = hiltViewModel()
) {
    val navigationManager = LocalNavManager.current
    val state by viewModel.state.collectAsState()


    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                RegisterEffect.NavigateToSignIn -> navigationManager.navigateUp()
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
