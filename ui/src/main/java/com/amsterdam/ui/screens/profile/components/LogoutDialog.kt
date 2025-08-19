package com.amsterdam.ui.screens.profile.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.amsterdam.designsystem.components.Dialog
import com.amsterdam.designsystem.components.Text
import com.amsterdam.designsystem.components.buttons.ConfirmButton
import com.amsterdam.designsystem.theme.AflamiTheme
import com.amsterdam.designsystem.theme.AppTheme
import com.amsterdam.designsystem.utils.ThemeAndLocalePreviews
import com.amsterdam.ui.R
import com.amsterdam.ui.components.DialogTitleRow

@Composable
fun LogoutDialog(
    isLogoutButtonLoading: Boolean,
    modifier: Modifier = Modifier,
    onLogoutClick: () -> Unit,
    onDismissClick: () -> Unit
){
    Dialog(
        onDismiss = onDismissClick
    ) {
        Column(
            modifier = modifier.padding(12.dp)
                .background(AppTheme.color.surface),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            DialogTitleRow(
                modifier = Modifier.padding(bottom = 24.dp),
                title = stringResource(R.string.logout),
                onDismiss = onDismissClick
            )
            Image(
                painter = painterResource(R.drawable.logout_warning),
                modifier = Modifier.height(100.dp),
                contentScale = ContentScale.FillHeight,
                contentDescription = null
            )
            Text(
                modifier = Modifier.padding(top = 12.dp),
                text = stringResource(R.string.confirm_message),
                style = AppTheme.textStyle.title.small,
                color = AppTheme.color.body,
                textAlign = TextAlign.Center
            )
            Text(
                modifier = Modifier.padding(bottom = 24.dp),
                text = stringResource(R.string.logout_message),
                style = AppTheme.textStyle.title.small,
                color = AppTheme.color.body,
                textAlign = TextAlign.Center
            )
            ConfirmButton(
                title = stringResource(R.string.logout),
                onClick = onLogoutClick,
                isEnabled = true,
                isLoading = isLogoutButtonLoading,
                isNegative = true,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@ThemeAndLocalePreviews
@Composable
private fun LogoutDialogPreview(){
    AflamiTheme {
        LogoutDialog(
            isLogoutButtonLoading = false,
            onLogoutClick = {},
            onDismissClick = {}
        )
    }
}