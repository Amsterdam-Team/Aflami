package com.example.designsystem.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.designsystem.theme.AflamiTheme
import com.example.designsystem.theme.AppTheme
import com.example.designsystem.utils.ThemeAndLocalePreviews

@Composable
fun DropdownMenuItem(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    androidx.compose.material3.DropdownMenuItem(
        text = {
            Text(
                text = text,
                style = AppTheme.textStyle.body.medium,
                color = AppTheme.color.body
            )
        },
        onClick = onClick,
        modifier = modifier
    )
}

@ThemeAndLocalePreviews
@Composable
private fun DropdownMenuItemPreview() {
    AflamiTheme {
        DropdownMenuItem(
            "item", onClick = {}
        )
    }
}


