package com.example.ui.screens.topRated.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.designsystem.theme.AflamiTheme
import com.example.designsystem.theme.AppTheme
import com.example.designsystem.utils.ThemeAndLocalePreviews
import com.example.ui.R
import io.sifr.shaded.modifiers.blur

@Composable
fun TopRatedBackgroundComponent() {
    AppTheme.color.topRatedGradientBackground
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(.593f)
            .background(
                brush = Brush.verticalGradient(
                    colors = AppTheme.color.topRatedGradientBackground
                )
            )
    ) {
        FireImage(
            size = 64.dp,
            alignment = Alignment.TopEnd,
            offsetX = 8.dp,
            offsetY = 59.dp
        )
        FireImage(
            size = 32.dp,
            alignment = Alignment.TopEnd,
            offsetX = (-34).dp,
            offsetY = 39.dp
        )
        FireImage(
            size = 24.dp,
            alignment = Alignment.TopEnd,
            offsetX = (-60).dp,
            offsetY = 20.dp
        )
        FireImage(
            size = 200.dp,
            alignment = Alignment.BottomCenter,
            offsetX = 0.dp,
            offsetY = (-76).dp
        )
    }
}

@Composable
private fun BoxScope.FireImage(
    size: Dp,
    alignment: Alignment,
    offsetX: Dp,
    offsetY: Dp
) {
    Image(
        painter = painterResource(id = R.drawable.fire),
        modifier = Modifier
            .size(size)
            .align(alignment)
            .offset(x = offsetX, y = offsetY)
            .blur(3f),
        contentDescription = null
    )
}


@ThemeAndLocalePreviews
@Composable
private fun TopRatedBackgroundComponentPreview() {
    AflamiTheme {
        TopRatedBackgroundComponent()
    }
}