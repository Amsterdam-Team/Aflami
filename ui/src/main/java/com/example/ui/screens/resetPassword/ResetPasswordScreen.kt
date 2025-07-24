package com.example.ui.screens.ResetPassword

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
import com.example.ui.screens.register.components.ResetPasswordWebView
import com.example.viewmodel.resetPassword.ResetPasswordEffect
import com.example.viewmodel.resetPassword.ResetPasswordUiState
import com.example.viewmodel.resetPassword.ResetPasswordViewModel
import org.koin.androidx.compose.koinViewModel


@Composable
fun ResetPasswordScreen(
    viewModel: ResetPasswordViewModel = koinViewModel()
) {
    val navController = LocalNavController.current
    val state by viewModel.state.collectAsState()


    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                ResetPasswordEffect.NavigateToSignIn -> navController.popBackStack()
                else -> {}
            }
        }
    }

    ResetPasswordContent(
        state = state,
        onLoadingChanged = viewModel::setLoading,
        onResetPasswordComplete = viewModel::onResetPasswordComplete
    )
}

@SuppressLint("JavascriptInterface")
@Composable
private fun ResetPasswordContent(
    state: ResetPasswordUiState,
    onLoadingChanged: (Boolean) -> Unit,
    onResetPasswordComplete: () -> Unit
) {

    Box(
        Modifier
            .fillMaxSize()
            .background(AppTheme.color.surface)
            .statusBarsPadding()
            .navigationBarsPadding(),
        contentAlignment = Alignment.Center
    ) {
        ResetPasswordWebView(
            modifier = Modifier.fillMaxSize(),
            onLoadingStateChanged = onLoadingChanged,
            urlToLoad = state.resetPasswordUrl,
            onResetPasswordComplete = onResetPasswordComplete
        )

        AnimatedSectionVisibility(state.isLoading) {
            LoadingContainer()
        }

    }
}
