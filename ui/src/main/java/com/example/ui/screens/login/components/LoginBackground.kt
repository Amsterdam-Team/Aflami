package com.example.ui.screens.login.components

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.designsystem.theme.AflamiTheme
import com.example.designsystem.theme.AppTheme

@Composable
private fun BackgroundCircle(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(
                brush = Brush.linearGradient(
                    0f to AppTheme.color.backgroundCircles,
                    1f to AppTheme.color.backgroundCircles
                ),
                alpha = 0.16f
            )
    )
}

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun LoginBackground() {

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    val screenHeight = configuration.screenHeightDp
    val circles = getBackgroundCircles()

    Box(
        modifier = Modifier
            .background(
                brush = Brush.verticalGradient(
                    0f to AppTheme.color.loginGradient.first(),
                    0.775f to AppTheme.color.loginGradient.last()
                )
            )
            .fillMaxSize(),
    ) {
        repeat(circles.size) { index ->
            val circle = circles[index]
            val size = circle.size
            val xOffset = (circle.xCoord / 360f) * screenWidth
            val yOffset = (circle.yCoord / 800f) * screenHeight
            BackgroundCircle(
                modifier = Modifier
                    .size(size)
                    .offset(x = xOffset.dp, y = yOffset.dp)
            )
        }
    }
}


@Preview(
    showBackground = true,
    backgroundColor = 0xFF0D090B,
)
@Composable
private fun LoginBackgroundPreview() {
    AflamiTheme(isDarkTheme = true) {
        LoginBackground()
    }
}