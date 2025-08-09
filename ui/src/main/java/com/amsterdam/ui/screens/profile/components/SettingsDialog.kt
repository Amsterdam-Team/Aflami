package com.amsterdam.ui.screens.profile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.amsterdam.designsystem.components.Dialog
import com.amsterdam.designsystem.components.Icon
import com.amsterdam.designsystem.components.buttons.PlainTextButton
import com.amsterdam.designsystem.theme.AflamiTheme
import com.amsterdam.designsystem.theme.AppTheme
import com.amsterdam.designsystem.utils.ThemeAndLocalePreviews
import com.amsterdam.ui.R
import com.amsterdam.ui.components.DialogTitleRow
import com.amsterdam.designsystem.R as R2

@Composable
fun SettingsDialog(
    modifier: Modifier = Modifier,
    onChangePasswordClick: () -> Unit,
    onContentRestrictionClick: () -> Unit,
    onLogoutClick: () -> Unit,
    onDismissClick: () -> Unit
) {
    Dialog(
        onDismiss = onDismissClick
    ) {
        Column(
            modifier = modifier
                .padding(vertical = 12.dp)
                .background(AppTheme.color.surface)
        ) {
            DialogTitleRow(
                title = stringResource(R.string.settings),
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .padding(bottom = 24.dp),
                onDismiss = onDismissClick
            )
            OptionRow(
                modifier = Modifier.padding(bottom = 12.dp),
                title = stringResource(R.string.change_password),
                leadingIcon = painterResource(R.drawable.ic_change_password),
                trailingContent = {
                    Icon(
                        painter = painterResource(R2.drawable.ic_arrow_right),
                        tint = AppTheme.color.hint,
                        modifier = Modifier.size(20.dp),
                        contentDescription = null
                    )
                },
                onClick = onChangePasswordClick
            )
            OptionRow(
                modifier = Modifier.padding(bottom = 12.dp),
                title = stringResource(R.string.content_restriction),
                leadingIcon = painterResource(R.drawable.ic_restriction),
                trailingContent = {
                    Icon(
                        painter = painterResource(R2.drawable.ic_arrow_right),
                        tint = AppTheme.color.hint,
                        modifier = Modifier.size(20.dp),
                        contentDescription = null
                    )
                },
                onClick = onContentRestrictionClick
            )
            OptionRow(
                title = stringResource(R.string.tired_of_watching),
                leadingIcon = painterResource(R.drawable.ic_logout),
                trailingContent = {
                    PlainTextButton(
                        title = stringResource(R.string.logout),
                        style = AppTheme.textStyle.label.medium,
                        onClick = onLogoutClick,
                        isLoading = false,
                        isEnabled = true,
                        isNegative = false,
                    )
                },
            )
        }
    }
}

@ThemeAndLocalePreviews
@Composable
private fun SettingsDialogPreview() {
    AflamiTheme {
        SettingsDialog(
            onChangePasswordClick = {},
            onContentRestrictionClick = {},
            onLogoutClick = {},
            onDismissClick = {},
        )
    }
}