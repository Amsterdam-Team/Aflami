package com.example.designsystem.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.designsystem.R
import com.example.designsystem.theme.AppTheme
import com.example.designsystem.utils.ThemeAndLocalePreviews

@Composable
fun LoadingIndicator(
    modifier: Modifier = Modifier,
    tint: Color = AppTheme.color.primary,
    size: Dp = 48.dp,
) {
    val angle by rememberInfiniteTransition().animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
    )

    val steppedAngle = (angle / 45f).toInt() * 45f

    Icon(
        imageVector = ImageVector.vectorResource(id = R.drawable.ic_loading),
        contentDescription = null,
        tint = tint,
        modifier = modifier.size(size).rotate(steppedAngle),
    )
}

@ThemeAndLocalePreviews
@Composable
private fun LoadingIndicatorPreview() {
    LoadingIndicator()
}