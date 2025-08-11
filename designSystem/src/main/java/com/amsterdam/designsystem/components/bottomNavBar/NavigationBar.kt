package com.amsterdam.designsystem.components.bottomNavBar

import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.amsterdam.designsystem.theme.AppTheme
import androidx.compose.material3.NavigationBar as MaterialNavigationBar


@Composable
fun NavigationBar(
    modifier: Modifier = Modifier,
    containerColor: Color = Color.Unspecified,
    content: @Composable() (RowScope.() -> Unit)
) {
    val borderColor = AppTheme.color.stroke
    val strokeWidth = 1.dp

    MaterialNavigationBar(
        modifier = modifier.drawBehind {
            val strokeWidthPx = strokeWidth.toPx()
            drawLine(
                color = borderColor,
                start = Offset(0f, 0f),
                end = Offset(size.width, 0f),
                strokeWidth = strokeWidthPx
            )
        },
        containerColor = containerColor,
        content = content
    )
}
