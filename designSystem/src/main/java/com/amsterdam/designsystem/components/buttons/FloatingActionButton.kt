package com.amsterdam.designsystem.components.buttons

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.amsterdam.designsystem.R
import com.amsterdam.designsystem.theme.AflamiTheme
import com.amsterdam.designsystem.utils.ThemeAndLocalePreviews

@Composable
fun FloatingActionButton(
    @DrawableRes icon: Int,
    onClick: () -> Unit,
    isLoading: Boolean,
    isNegative: Boolean,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
) {
    BaseButton(
        icon = icon,
        iconSize = 24.dp,
        height = 64.dp,
        width = 64.dp,
        onClick = onClick,
        isLoading = isLoading,
        isEnabled = isEnabled,
        isNegative = isNegative,
        isSecondary = false,
        modifier = modifier,
    )
}

@ThemeAndLocalePreviews
@Composable
private fun FilledButtonPreview() {
    AflamiTheme {
        Box(
            modifier = Modifier.padding(16.dp),
        ) {
            FloatingActionButton(
                icon = R.drawable.ic_download,
                onClick = {},
                isLoading = false,
                isNegative = false,
                modifier = Modifier,
            )
        }
    }
}
