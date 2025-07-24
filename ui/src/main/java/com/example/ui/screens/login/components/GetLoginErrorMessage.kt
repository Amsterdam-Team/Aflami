package com.example.ui.screens.login.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.ui.R
import com.example.viewmodel.login.PasswordErrorState
import com.example.viewmodel.login.UsernameErrorState

@Composable
fun getUserNameErrorMessage(usernameErrorState: UsernameErrorState?): String{
    return when(usernameErrorState){
        UsernameErrorState.InvalidCredentials -> stringResource(R.string.incorrect_username_or_password)
        null -> ""
    }
}

@Composable
fun getPasswordErrorMessage(passwordErrorState: PasswordErrorState?): String{
    return when(passwordErrorState){
        PasswordErrorState.InvalidCredentials -> stringResource(R.string.incorrect_username_or_password)
        null -> ""
    }
}