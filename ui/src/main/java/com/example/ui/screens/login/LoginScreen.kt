package com.example.ui.screens.login

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import com.example.designsystem.components.IconButton
import com.example.designsystem.components.Text
import com.example.designsystem.components.TextField
import com.example.designsystem.components.buttons.ButtonDefaults
import com.example.designsystem.components.buttons.ConfirmButton
import com.example.designsystem.components.buttons.OutlinedButton
import com.example.designsystem.components.buttons.PlainTextButton
import com.example.designsystem.theme.AflamiTheme
import com.example.designsystem.theme.AppTheme
import com.example.designsystem.utils.ThemeAndLocalePreviews
import com.example.ui.R
import com.example.ui.application.LocalNavController
import com.example.ui.navigation.Route
import com.example.ui.screens.login.components.LoginBackground
import com.example.ui.screens.login.components.getPasswordTextFieldIcon
import com.example.ui.utils.safeNavigateToTab
import com.example.viewmodel.login.LoginEffect
import com.example.viewmodel.login.LoginInteractionListener
import com.example.viewmodel.login.LoginUiState
import com.example.viewmodel.login.LoginViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()
    val navController = LocalNavController.current
    LaunchedEffect(Unit) {
        viewModel.effect.collect{ effect ->
            effect?.let {
                when(it){
                    LoginEffect.NavigateToHome -> {
                        navController.safeNavigateToTab(Route.Tab.Home)
                    }
                }
            }
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
    Box {
        LoginBackground()
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp)
                .statusBarsPadding()
                .navigationBarsPadding()
                .verticalScroll(scrollState)
            ,
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
                    modifier = Modifier.padding(bottom = 24.dp)
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
                    errorMessage = state.usernameError,
                    isError = state.usernameError.isNotBlank(),
                    leadingIcon = com.example.designsystem.R.drawable.ic_user_square,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
                TextField(
                    text = state.password,
                    onValueChange = interactionListener::onPasswordUpdate,
                    hintText = stringResource(R.string.password_hint),
                    errorMessage = state.passwordError,
                    isError = state.passwordError.isNotBlank(),
                    isTrailingClickEnabled = true,
                    onTrailingClick = interactionListener::onShowPasswordClicked,
                    leadingIcon = com.example.designsystem.R.drawable.ic_door_lock,
                    trailingIcon = getPasswordTextFieldIcon(state.isPasswordShown),
                    isObscured = !state.isPasswordShown,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                PlainTextButton(
                    stringResource(R.string.forgot_password),
                    onClick = interactionListener::onForgotPasswordClicked,
                    style = AppTheme.textStyle.label.medium,
                    isLoading = false,
                    isEnabled = true,
                    isNegative = false,
                    modifier = Modifier.align(Alignment.End).padding(bottom = 48.dp),
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
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
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
                password = "mypassword",
                passwordError = "Password is incorrect",
            ),
            interactionListener = object : LoginInteractionListener {
                override fun onUserNameUpdated(username: String) {
                    TODO("Not yet implemented")
                }

                override fun onPasswordUpdate(password: String) {
                    TODO("Not yet implemented")
                }

                override fun onShowPasswordClicked() {
                    TODO("Not yet implemented")
                }

                override fun onLoginClicked() {
                    TODO("Not yet implemented")
                }

                override fun onContinueAsGuestClicked() {
                    TODO("Not yet implemented")
                }

                override fun onForgotPasswordClicked() {
                    TODO("Not yet implemented")
                }

                override fun onCreateAccountClicked() {
                    TODO("Not yet implemented")
                }
            }
        )
    }
}