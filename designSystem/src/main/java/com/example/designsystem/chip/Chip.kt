package com.example.designsystem.chip

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
import com.example.designsystem.R
import com.example.designsystem.theme.AflamiTheme
import com.example.designsystem.theme.AppTheme
import com.example.designsystem.utils.ThemeAndLocalePreviews

@Composable
fun Chip(
    icon: Painter,
    label: String,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    colors: ChipColor = ChipDefaults.chipColors(),
    onClick: () -> Unit = {},
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) colors.backgroundSelectedColor else colors.backgroundUnselectedColor,
        animationSpec = tween(durationMillis = 500),
        label = "BackgroundColorAnimation",
    )

    val iconColor = if (isSelected) colors.iconSelectedColor else colors.iconUnselectedColor
    val labelColor = if (isSelected) colors.labelSelectedColor else colors.labelUnselectedColor
    val borderColor = if (isSelected) colors.borderSelectedColor else colors.borderUnselectedColor
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier,
    ) {
        Box(
            modifier =
                Modifier
                    .size(56.dp)
                    .background(
                        backgroundColor,
                        RoundedCornerShape(16.dp),
                    )
                    .border(1.dp, borderColor, RoundedCornerShape(16.dp))
                    .clip(RoundedCornerShape(16.dp))
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = ripple(color = iconColor, bounded = true),
                        onClick = onClick,
                    ),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                painter = icon,
                contentDescription = label,
                tint = iconColor,
                modifier = Modifier.size(24.dp),
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
            icon =
                painterResource(
                    R.drawable.ic_menu_square,
                ),
            label = "All",
            isSelected = true,
        )
    }
}
