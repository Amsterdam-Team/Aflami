package com.amsterdam.ui.screens.profile.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.amsterdam.designsystem.components.Dialog
import com.amsterdam.designsystem.components.IconButton
import com.amsterdam.designsystem.components.Text
import com.amsterdam.designsystem.components.buttons.ConfirmButton
import com.amsterdam.designsystem.theme.AppTheme
import com.amsterdam.designsystem.utils.ThemeAndLocalePreviews
import com.amsterdam.ui.R
import com.amsterdam.ui.components.selection.ThemeSelectionItem

@Composable
fun ThemeDialog(
    isDarkTheme: Boolean,
    modifier: Modifier = Modifier,
    onChangeTheme: (Boolean) -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {

    Dialog(
        onDismiss = onDismiss,
        isDismissible = true,
        modifier = modifier,
    ) {
        DialogContent(
            isDarkTheme = isDarkTheme,
            onConfirm = { onConfirm() },
            onDismiss = { onDismiss() },
            onChangeTheme = { onChangeTheme(it) })
    }
}


@Composable
fun DialogContent(
    isDarkTheme: Boolean,
    onConfirm: () -> Unit,
    onChangeTheme: (Boolean) -> Unit,
    onDismiss: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(R.string.app_theme),
                style = AppTheme.textStyle.title.large,
                color = AppTheme.color.title
            )
            IconButton(
                painter = painterResource(com.amsterdam.designsystem.R.drawable.ic_cancel),
                contentDescription = stringResource(R.string.cancel),
                onClick = {
                    onDismiss()
                },
                tint = AppTheme.color.title,
            )
        }

        ThemeSelectionItem(
            modifier = Modifier.padding(top = 24.dp),
            isSelected = isDarkTheme,
            onClick = { onChangeTheme(true) },
            text = stringResource(R.string.dark),
            trailingIcon = com.amsterdam.designsystem.R.drawable.ic_moon,
        )

        ThemeSelectionItem(
            modifier = Modifier.padding(top = 12.dp),
            isSelected = !isDarkTheme,
            onClick = { onChangeTheme(false) },
            text = stringResource(R.string.light),
            trailingIcon = com.amsterdam.designsystem.R.drawable.ic_sun,
        )
        AnimatedVisibility(
            visible = !isDarkTheme,
            modifier = Modifier,
            enter = fadeIn(tween(durationMillis = 300)) + expandVertically(tween(durationMillis = 300)),
            exit = fadeOut(tween(durationMillis = 300)) + shrinkVertically(tween(durationMillis = 300))
        ) {
            LightContent(
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        ConfirmButton(
            title = stringResource(R.string.apply),
            onClick = { onConfirm() },
            isEnabled = true,
            isLoading = false,
            isNegative = false,
            modifier = Modifier.padding(top = 24.dp),
        )
    }
}

@Composable
private fun LightContent(
    modifier: Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(shape = RoundedCornerShape(12.dp), color = AppTheme.color.surfaceHigh)
            .padding(8.dp),

        ) {
        Image(
            painter = painterResource(com.amsterdam.designsystem.R.drawable.ic_stars),
            contentDescription = stringResource(R.string.profile),
            colorFilter = ColorFilter.tint(AppTheme.color.yellowAccent),
            modifier = Modifier
                .padding(end = 8.dp)
                .size(24.dp)
        )

        Text(
            text = stringResource(R.string.brightness_message),
            style = AppTheme.textStyle.label.small,
            color = AppTheme.color.yellowAccent,
            modifier = Modifier.align(Alignment.CenterVertically)
        )
    }
}


@ThemeAndLocalePreviews
@Composable
private fun ThemeDialogPreview() {
    ThemeDialog(
        isDarkTheme = true,
        onChangeTheme = {},
        onConfirm = {},
        onDismiss = {}
    )
}