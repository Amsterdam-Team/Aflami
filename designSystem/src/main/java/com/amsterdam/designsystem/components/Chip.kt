package com.amsterdam.designsystem.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.amsterdam.designsystem.R
import com.amsterdam.designsystem.theme.AflamiTheme
import com.amsterdam.designsystem.theme.AppTheme
import com.amsterdam.designsystem.utils.ThemeAndLocalePreviews

@Composable
fun Chip(
    icon: Painter,
    label: String,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) AppTheme.color.secondary else AppTheme.color.surfaceHigh,
        animationSpec = tween(durationMillis = 500),
        label = "BackgroundColorAnimation"
    )

    val iconColor = if (isSelected) AppTheme.color.onPrimary else AppTheme.color.hint
    val labelColor = if (isSelected) AppTheme.color.body else AppTheme.color.hint
    val borderColor = if (isSelected) AppTheme.color.stroke else AppTheme.color.surfaceHigh
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = modifier
                .size(56.dp)
                .background(
                    backgroundColor,
                    RoundedCornerShape(16.dp)
                )
                .border(1.dp, borderColor, RoundedCornerShape(16.dp))
                .clip(RoundedCornerShape(16.dp))
                .clickable( interactionSource =  remember { MutableInteractionSource() },
                    indication = ripple(color = iconColor,bounded = true)
                    ,onClick = onClick),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = icon, contentDescription = label, tint = iconColor,
                modifier = Modifier.size(24.dp)
            )
        }
        Text(
            text = label,
            color = labelColor,
            style = AppTheme.textStyle.label.small,
        )
    }
}

@ThemeAndLocalePreviews
@Composable
private fun ChipPreview() {
    AflamiTheme {
            Chip(
                icon = painterResource(
                    R.drawable.ic_menu_square
                ),
                label = "All",
                isSelected = true
            )
    }
}