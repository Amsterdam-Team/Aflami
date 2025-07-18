package com.example.ui.screens.movieDetails.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.designsystem.theme.AppTheme
import com.example.designsystem.utils.dropShadow
import com.example.designsystem.components.Icon
import com.example.designsystem.R

@Composable
fun PlayButton(modifier: Modifier = Modifier,isActive : Boolean) {
    val playButtonColor = if(isActive) AppTheme.color.primary else AppTheme.color.disable
    val playButtonBlurColor = if(isActive) Color(0x1F8951FF) else  Color.Transparent
    Box(
        modifier = modifier
            .size(64.dp)
            .clip(CircleShape)
            .background(
                color = AppTheme.color.surfaceHigh,
                shape = CircleShape
            )
            .padding(4.dp)
            .border(2.dp, AppTheme.color.stroke, shape = CircleShape)
            .dropShadow(CircleShape,playButtonBlurColor , spread = 12.dp, blur = 24.dp,
                offsetX = 0.dp, offsetY = 4.dp)
            .background(
                color = AppTheme.color.surfaceHigh,
                shape = CircleShape
            )

    ) {
        Icon(
            painterResource(R.drawable.ic_play),
            contentDescription = null,
            tint = playButtonColor,
            modifier = Modifier
                .size(24.dp)
                .align(Alignment.Center),
        )
    }
}