package com.example.designsystem.components.divider

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.designsystem.theme.AflamiTheme
import com.example.designsystem.theme.AppTheme
import com.example.designsystem.utils.ThemeAndLocalePreviews

@Composable
fun VerticalDivider(
    modifier: Modifier = Modifier,
    thickness: Dp = 2.dp,
    color: Color = AppTheme.color.stroke,
) {
    Canvas(
        modifier
            .fillMaxHeight()
            .width(thickness),
    ) {
        drawLine(
            color = color,
            strokeWidth = thickness.toPx(),
            start = Offset(thickness.toPx() / 2, 0f),
            end = Offset(thickness.toPx() / 2, size.height),
        )
    }
}

@ThemeAndLocalePreviews
@Composable
private fun VerticalDividerPreview() {
    AflamiTheme {
        VerticalDivider()
    }
}
