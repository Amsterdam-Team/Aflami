package com.example.designsystem.components.buttons

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.designsystem.R
import com.example.designsystem.components.Icon
import com.example.designsystem.theme.AflamiTheme
import com.example.designsystem.utils.ThemeAndLocalePreviews

@Composable
fun FloatingActionButton(
    icon: @Composable (tint: Color) -> Unit,
    onClick: () -> Unit,
    isLoading: Boolean,
    isNegative: Boolean,
    modifier: Modifier = Modifier,
    colors: ButtonColors = ButtonDefaults.buttonColors(),
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
        colors = colors,
    )
}

@ThemeAndLocalePreviews
@Composable
private fun FloatingActionButtonPreview() {
    AflamiTheme {
        Box(
            modifier = Modifier.padding(16.dp),
        ) {
            FloatingActionButton(
                icon = { tint ->
                    Icon(
                        painter = painterResource(R.drawable.ic_download),
                        contentDescription = null,
                        tint = tint,
                    )
                },
                onClick = {},
                isLoading = false,
                isNegative = false,
                modifier = Modifier,
            )
        }
    }
}
