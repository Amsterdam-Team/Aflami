package com.example.designsystem.components.buttons

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.designsystem.R
import com.example.designsystem.theme.AflamiTheme
import com.example.designsystem.utils.ThemeAndLocalePreviews

@Composable
fun OutlinedButton(
    title: String,
    onClick: () -> Unit,
    isEnabled: Boolean,
    isLoading: Boolean,
    isNegative: Boolean,
    modifier: Modifier = Modifier,
    colors: ButtonColors = ButtonDefaults.buttonColors(),
    icon: (@Composable (tint: Color) -> Unit)? = null,
) {
    BaseButton(
        title = title,
        icon = icon,
        onClick = onClick,
        isLoading = isLoading,
        isEnabled = isEnabled,
        isNegative = isNegative,
        isSecondary = true,
        modifier = modifier,
        colors = colors,
    )
}

@ThemeAndLocalePreviews
@Composable
private fun OutlinedButtonPreview() {
    AflamiTheme {
        Box(
            modifier = Modifier.padding(16.dp),
        ) {
            OutlinedButton(
                title = stringResource(R.string.add),
                onClick = {},
                isEnabled = true,
                isLoading = false,
                isNegative = false,
                modifier = Modifier,
            )
        }
    }
}
