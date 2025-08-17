package com.amsterdam.ui.screens.login.components

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.amsterdam.designsystem.theme.AflamiTheme
import com.amsterdam.designsystem.theme.AppTheme
import com.amsterdam.designsystem.utils.ThemeAndLocalePreviews

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
    val layoutDirection = LocalLayoutDirection.current

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
            var xOffset = (circle.xCoord / 360f) * screenWidth
            val yOffset = (circle.yCoord / 800f) * screenHeight
            if (layoutDirection == LayoutDirection.Rtl){
                xOffset = screenWidth - xOffset - size.value
            }
            BackgroundCircle(
                modifier = Modifier
                    .size(size)
                    .offset(x = xOffset.dp, y = yOffset.dp)
            )
        }
    }
}

@ThemeAndLocalePreviews
@Composable
private fun LoginBackgroundPreview() {
    AflamiTheme(isDarkTheme = true) {
            LoginBackground()
    }
}