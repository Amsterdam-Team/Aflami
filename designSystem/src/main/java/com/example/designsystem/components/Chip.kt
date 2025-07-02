package com.example.designsystem.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.designsystem.R
import com.example.designsystem.theme.AflamiTheme
import com.example.designsystem.theme.AppTheme
import com.example.designsystem.utils.ThemeAndLocalePreviews

@Composable
fun Chip(
    modifier: Modifier = Modifier,
    icon: Painter,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit = {}
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) AppTheme.color.secondary else AppTheme.color.surfaceHigh,
        animationSpec = tween(durationMillis = 500),
        label = "BackgroundColorAnimation"
    )

    val iconColor = if (isSelected) AppTheme.color.onPrimary else AppTheme.color.hint
    val labelColor = if (isSelected) AppTheme.color.body else AppTheme.color.hint
    val borderColor = if (isSelected) AppTheme.color.stroke else AppTheme.color.surfaceHigh
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = modifier
                .size(56.dp)
                .background(
                    backgroundColor,
                    RoundedCornerShape(16.dp)
                )
                .border(1.dp, borderColor, RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = icon, contentDescription = label, tint = iconColor,
                modifier = Modifier.size(24.dp)
            )
        }
        Text(
            text = label,
            color = labelColor,
            style = AppTheme.textStyle.label.small,
            modifier = Modifier.clickable(onClick = onClick)
        )
    }
}

@ThemeAndLocalePreviews
@Composable
private fun ChipPreview() {
    AflamiTheme {
            Chip(
                icon = painterResource(
                    R.drawable.ic_menu_square
                ),
                label = "All",
                isSelected = true
            )
    }
}