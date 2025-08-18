package com.amsterdam.ui.components.movieAndTvShowDetails

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.amsterdam.designsystem.R
import com.amsterdam.designsystem.components.Icon
import com.amsterdam.designsystem.theme.AppTheme
import com.amsterdam.designsystem.utils.modifierExtensions.dropShadow

@Composable
fun PlayButton(
    isActive: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    val playButtonColor = if (isActive) AppTheme.color.primary else AppTheme.color.disable
    val playButtonBlurColor = if (isActive) AppTheme.color.droppedShadowColor else Color.Transparent
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Box(
            modifier = Modifier
                .size(72.dp)
                .background(
                    color = AppTheme.color.surface,
                    shape = CircleShape,
                )
        )
        Box(
            modifier = Modifier
                .size(58.dp)
                .dropShadow(
                    CircleShape,
                    playButtonBlurColor,
                    spread = 0.dp,
                    blur = 12.dp,
                    offsetX = 0.dp,
                    offsetY = 2.dp,
                )
        )
        Box(
            modifier = Modifier
                .size(64.dp)
                .background(
                    color = AppTheme.color.surfaceHigh,
                    shape = CircleShape,
                )
                .border(2.dp, AppTheme.color.stroke, shape = CircleShape)
        ) {
            Icon(
                painterResource(R.drawable.ic_play),
                contentDescription = null,
                tint = playButtonColor,
                modifier = Modifier.size(24.dp).align(Alignment.Center).let {
                    if (isActive) it.clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() },
                        onClick = onClick
                    )
                    else it
                })
        }
    }
}
