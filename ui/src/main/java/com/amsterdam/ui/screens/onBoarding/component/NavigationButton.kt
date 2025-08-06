package com.amsterdam.ui.screens.onBoarding.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.amsterdam.designsystem.R
import com.amsterdam.designsystem.components.Icon
import com.amsterdam.designsystem.theme.AflamiTheme
import com.amsterdam.designsystem.theme.AppTheme
import com.amsterdam.designsystem.utils.ThemeAndLocalePreviews
import com.amsterdam.designsystem.utils.ripple
import kotlinx.coroutines.delay

@Composable
fun NavigationButton(
    icon: Painter,
    modifier: Modifier = Modifier,
    iconModifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    val interactionSource = remember { MutableInteractionSource() }
    var isPressed by remember { mutableStateOf(false) }

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.85f else 1f,
        animationSpec = tween(
            durationMillis = 300,
            easing = {
                val overshoot = 1.2f
                val x = it - 1f
                x * x * ((overshoot + 1) * x + overshoot) + 1f
            }
        ),
        label = "scaleAnimation"
    )

    Box(
        modifier = modifier
            .size(width = 68.dp, height = 56.dp)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .shadow(
                elevation = 16.dp,
                shape = RoundedCornerShape(16.dp),
                ambientColor = AppTheme.color.primary.copy(alpha = 0.4f),
                spotColor = AppTheme.color.primary.copy(alpha = 0.4f)
            )
            .background(
                color = AppTheme.color.primaryVariant,
                shape = RoundedCornerShape(16.dp)
            )
            .clip(RoundedCornerShape(16.dp))
            .clickable(
                interactionSource = interactionSource,
                indication = ripple(color = AppTheme.color.primary, bounded = true),
                onClick = {
                    isPressed = true
                    onClick()
                }
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = icon,
            contentDescription = "Navigation Button Icon",
            tint = AppTheme.color.primary,
            modifier = iconModifier.size(20.dp)
        )
    }

    LaunchedEffect(isPressed) {
        if (isPressed) {
            delay(250)
            isPressed = false
        }
    }
}


@ThemeAndLocalePreviews
@Composable
private fun NavigationButtonPreview() {
    AflamiTheme {
        Row(horizontalArrangement = Arrangement.spacedBy(18.dp)) {
            NavigationButton(
                icon = painterResource(R.drawable.ic_menu_square),
            )
        }
    }
}