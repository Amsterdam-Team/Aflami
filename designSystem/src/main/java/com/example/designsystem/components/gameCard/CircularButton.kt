package com.example.designsystem.components.gameCard

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.designsystem.theme.AppTheme

@Composable
fun CircularButton(
    modifier: Modifier = Modifier,
    clickable: Boolean = true,
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    Row(
        modifier = modifier
            .wrapContentSize()
            .background(color = AppTheme.color.onPrimaryButton, shape = CircleShape)
            .border(
                width = .5.dp,
                brush = Brush.linearGradient(
                    colors = listOf(
                        AppTheme.color.onPrimaryButton, AppTheme.color.onPrimaryButton.copy(alpha = .24f)
                    )
                ),
                shape = CircleShape
            )
            .clickable(enabled = clickable, onClick = onClick)
            .padding(4.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        content()
    }
}