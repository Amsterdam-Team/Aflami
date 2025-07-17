package com.example.designsystem.components.buttons

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.designsystem.components.LoadingIndicator
import com.example.designsystem.theme.AppTheme
import com.example.designsystem.utils.ThemeAndLocalePreviews

@Composable
internal fun BaseButton(
    onClick: () -> Unit,
    isLoading: Boolean,
    isNegative: Boolean,
    isEnabled: Boolean,
    isSecondary: Boolean,
    modifier: Modifier = Modifier,
    title: String? = null,
    icon: (@Composable (tint: Color) -> Unit)? = null,
    iconSize: Dp = 20.dp,
    height: Dp = 56.dp,
    width: Dp = Dp.Unspecified,
) {
    val backGroundColor =
        animateButtonBrush(
            isEnabled = isEnabled,
            isNegative = isNegative,
            isSecondary = isSecondary,
        )
    val contentColor by animateColorAsState(
        when {
            !isEnabled -> AppTheme.color.stroke
            isNegative -> AppTheme.color.redAccent
            isSecondary -> AppTheme.color.primary
            else -> AppTheme.color.onPrimary
        },
    )

    Row(
        modifier =
            modifier
                .clip(RoundedCornerShape(16.dp))
                .clipToBounds()
                .width(width)
                .fillMaxWidth()
                .height(height)
                .background(backGroundColor)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = ripple(),
                    onClick = onClick,
                    enabled = isEnabled && !isLoading,
                ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        if (title != null) {
            Text(
                text = title,
                color = contentColor,
                style = AppTheme.textStyle.label.large,
            )
        }

        val animatedPadding by animateDpAsState(
            if (isLoading || icon != null) 8.dp else 0.dp,
            tween(durationMillis = 500),
        )
        val animatedIconSize by animateDpAsState(
            if (!isLoading) iconSize else 0.dp,
            tween(durationMillis = 500),
        )
        val animatedLoadingSize by animateDpAsState(
            if (isLoading) 20.dp else 0.dp,
            tween(durationMillis = 500),
        )

        AnimatedVisibility(
            visible = title != null && (icon != null || isLoading),
        ) {
            Box(Modifier.width(animatedPadding))
        }

        AnimatedVisibility(
            visible = icon != null && !isLoading,
            modifier = Modifier.size(animatedIconSize),
        ) {
            icon?.invoke(contentColor)
        }

        AnimatedVisibility(
            visible = isLoading && isEnabled,
            modifier = Modifier.size(animatedLoadingSize),
        ) {
            LoadingIndicator(tint = contentColor)
        }
    }
}

@Composable
private fun animateButtonBrush(
    isEnabled: Boolean,
    isNegative: Boolean,
    isSecondary: Boolean,
    animationSpec: AnimationSpec<Color> = tween(300),
): Brush {
    val startColor by animateColorAsState(
        targetValue =
            when {
                !isEnabled && !isSecondary -> AppTheme.color.disable
                isSecondary -> AppTheme.color.primaryVariant
                isNegative -> AppTheme.color.redVariant
                else -> AppTheme.color.primary
            },
        animationSpec = animationSpec,
        label = "startColor",
    )

    val endColor by animateColorAsState(
        targetValue =
            when {
                !isEnabled && !isSecondary -> AppTheme.color.disable
                isSecondary -> AppTheme.color.primaryVariant
                isNegative -> AppTheme.color.redVariant
                else -> AppTheme.color.primaryEnd
            },
        animationSpec = animationSpec,
        label = "endColor",
    )

    return Brush.verticalGradient(listOf(startColor, endColor))
}

@ThemeAndLocalePreviews
@Composable
private fun BaseButtonPreview() {
    BaseButton(
        onClick = {},
        isLoading = true,
        isNegative = true,
        isEnabled = true,
        isSecondary = false,
    )
}
