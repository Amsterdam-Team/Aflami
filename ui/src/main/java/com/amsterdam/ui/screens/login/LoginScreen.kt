package com.amsterdam.ui.screens.login

import android.content.res.Configuration.ORIENTATION_LANDSCAPE
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.amsterdam.designsystem.components.IconButton
import com.amsterdam.designsystem.components.Text
import com.amsterdam.designsystem.components.TextField
import com.amsterdam.designsystem.components.buttons.ButtonDefaults
import com.amsterdam.designsystem.components.buttons.ConfirmButton
import com.amsterdam.designsystem.components.buttons.OutlinedButton
import com.amsterdam.designsystem.components.buttons.PlainTextButton
import com.amsterdam.designsystem.components.snackBar.SnackBarManager
import com.amsterdam.designsystem.theme.AflamiTheme
import com.amsterdam.designsystem.theme.AppTheme
import com.amsterdam.designsystem.utils.ThemeAndLocalePreviews
import com.amsterdam.ui.R
import com.amsterdam.ui.application.LocalNavManager
import com.amsterdam.ui.screens.login.components.LoginBackground
import com.amsterdam.ui.screens.login.components.getLoginErrorMessage
import com.amsterdam.ui.screens.login.components.getPasswordTextFieldIcon
import com.amsterdam.viewmodel.login.LoginEffect
import com.amsterdam.viewmodel.login.LoginErrorState
import com.amsterdam.viewmodel.login.LoginInteractionListener
import com.amsterdam.viewmodel.login.LoginUiState
import com.amsterdam.viewmodel.login.LoginViewModel
import kotlinx.coroutines.delay

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val navigationManager = LocalNavManager.current
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                LoginEffect.NavigateToHome -> {
                    navigationManager.toHome(clearBackStack = true)
                }

                LoginEffect.NavigateToRegister -> navigationManager.toRegister()
                LoginEffect.NavigateToResetPassword -> navigationManager.toResetPassword()
                LoginEffect.ShowCredentialsError -> {
                }
            }
        }
    }

    LaunchedEffect(state.loginError) {
        if (state.loginError != null) {
            SnackBarManager.showError(
                getLoginErrorMessage(state.loginError, context)
            )
            viewModel.onLoginErrorHandled()
        }
    }

    LoginScreenContent(
        state = state,
        interactionListener = viewModel
    )
}

@Composable
private fun LoginScreenContent(
    modifier: Modifier = Modifier,
    state: LoginUiState,
    interactionListener: LoginInteractionListener
) {
    val scrollState = rememberScrollState()
    val isLandscape = LocalConfiguration.current.orientation == ORIENTATION_LANDSCAPE
    var show by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        delay(3000)
        show = true
    }
    Box {
        LoginBackground()
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp)
                .statusBarsPadding()
                .navigationBarsPadding()
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                IconButton(
                    iconSize = 48.dp,
                    painter = painterResource(R.drawable.app_logo),
                    contentDescription = stringResource(R.string.app_name),
                    containerColor = AppTheme.color.primaryVariant,
                    paddingValues = PaddingValues(horizontal = 6.dp, vertical = 9.dp),
                    withBorder = true,
                    modifier = Modifier.padding(vertical = 24.dp)
                )

                Text(
                    text = stringResource(R.string.welcome_back),
                    style = AppTheme.textStyle.title.medium,
                    color = AppTheme.color.title,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = stringResource(R.string.enter_info_to_login_message),
                    style = AppTheme.textStyle.body.medium,
                    color = AppTheme.color.body,
                    modifier = Modifier.padding(bottom = 24.dp)
                )
                TextField(
                    text = state.username,
                    onValueChange = interactionListener::onUserNameUpdated,
                    hintText = stringResource(R.string.user_name_hint),
                    leadingIcon = com.amsterdam.designsystem.R.drawable.ic_user_square,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
                TextField(
                    text = state.password,
                    onValueChange = interactionListener::onPasswordUpdate,
                    hintText = stringResource(R.string.password_hint),
                    isTrailingClickEnabled = true,
                    onTrailingClick = interactionListener::onShowPasswordClicked,
                    leadingIcon = com.amsterdam.designsystem.R.drawable.ic_door_lock,
                    trailingIcon = getPasswordTextFieldIcon(state.isPasswordShown),
                    isObscured = !state.isPasswordShown,
                    keyboardType = KeyboardType.Password,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                PlainTextButton(
                    stringResource(R.string.forgot_password),
                    onClick = interactionListener::onForgotPasswordClicked,
                    style = AppTheme.textStyle.label.medium,
                    isLoading = false,
                    isEnabled = true,
                    isNegative = false,
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(bottom = 48.dp),
                )
                ConfirmButton(
                    title = stringResource(R.string.login),
                    onClick = interactionListener::onLoginClicked,
                    isEnabled = state.isLoginButtonEnabled,
                    isLoading = state.isLoginButtonLoading,
                    isNegative = false,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
                OutlinedButton(
                    title = stringResource(R.string.continue_as_guest),
                    onClick = interactionListener::onContinueAsGuestClicked,
                    isEnabled = true,
                    isLoading = false,
                    isNegative = false,
                    colors = ButtonDefaults.buttonColors(containerColor = AppTheme.color.primaryVariant)
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
                    .then(
                        if (isLandscape) {
                            Modifier.padding(top = 32.dp)
                        } else Modifier
                    ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(R.string.dont_have_account),
                    style = AppTheme.textStyle.label.medium,
                    color = AppTheme.color.hint,
                    modifier = Modifier.padding(end = 4.dp)
                )

                PlainTextButton(
                    stringResource(R.string.create_account),
                    onClick = interactionListener::onCreateAccountClicked,
                    isLoading = false,
                    isEnabled = true,
                    isNegative = false,
                    style = AppTheme.textStyle.label.medium
                )
            }
        }
    }
}

@ThemeAndLocalePreviews
@PreviewScreenSizes
@Composable
private fun LoginScreenContentPreview() {
    AflamiTheme {
        LoginScreenContent(
            state = LoginUiState(
                password = "password",
                loginError = LoginErrorState.InvalidCredentials,
            ),
            interactionListener = object : LoginInteractionListener {
                override fun onUserNameUpdated(username: String) {}
                override fun onPasswordUpdate(password: String) {}
                override fun onShowPasswordClicked() {}
                override fun onLoginClicked() {}
                override fun onContinueAsGuestClicked() {}
                override fun onForgotPasswordClicked() {}
                override fun onCreateAccountClicked() {}
            }
        )
    }
}