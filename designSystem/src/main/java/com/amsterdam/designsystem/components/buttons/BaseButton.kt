package com.amsterdam.designsystem.components.buttons

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
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.amsterdam.designsystem.components.LoadingIndicator
import com.amsterdam.designsystem.theme.AppTheme
import com.amsterdam.designsystem.utils.ThemeAndLocalePreviews

@Composable
internal fun BaseButton(
    onClick: () -> Unit,
    isLoading: Boolean,
    isNegative: Boolean,
    isEnabled: Boolean,
    isSecondary: Boolean,
    modifier: Modifier = Modifier,
    colors: ButtonColors = ButtonDefaults.buttonColors(),
    title: String? = null,
    icon: (@Composable (tint: Color) -> Unit)? = null,
    iconSize: Dp = 20.dp
) {
    val backGroundColor =
        animateButtonBrush(
            isEnabled = isEnabled,
            isNegative = isNegative,
            isSecondary = isSecondary,
        )
    val contentColor by animateColorAsState(
        when {
            !isEnabled -> colors.disableContainerColor
            isNegative -> colors.negativeContainerColor
            isSecondary -> colors.secondaryContainerColor
            else -> colors.containerColor
        },
    )

    Row(
        modifier =
            modifier
                .background(brush = backGroundColor, shape = RoundedCornerShape(16.dp))
                .clip(RoundedCornerShape(16.dp))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = ripple(),
                    onClick = onClick,
                    enabled = isEnabled && !isLoading,
                )
                .padding(vertical = 16.dp,horizontal = 24.dp),
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
    colors: ButtonBrushColor = ButtonDefaults.brushColors(),
    animationSpec: AnimationSpec<Color> = tween(300),
): Brush {
    val startColor by animateColorAsState(
        targetValue =
            when {
                !isEnabled && !isSecondary -> colors.disableColor
                isSecondary -> colors.secondaryColor
                isNegative -> colors.negativeColor
                else -> colors.startColor
            },
        animationSpec = animationSpec,
        label = "startColor",
    )

    val endColor by animateColorAsState(
        targetValue =
            when {
                !isEnabled && !isSecondary -> colors.disableColor
                isSecondary -> colors.secondaryColor
                isNegative -> colors.negativeColor
                else -> colors.endColor
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
