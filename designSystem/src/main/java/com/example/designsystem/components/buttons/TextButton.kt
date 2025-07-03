package com.example.designsystem.components.buttons

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.designsystem.components.LoadingIndicator
import com.example.designsystem.theme.AflamiTheme
import com.example.designsystem.theme.AppTheme
import com.example.designsystem.utils.ThemeAndLocalePreviews

@Composable
fun TextButton(
    title: String,
    onClick: () -> Unit,
    isLoading: Boolean,
    isEnabled: Boolean,
    isNegative: Boolean,
    modifier: Modifier = Modifier,
) {
    val contentColor by animateColorAsState(
        when {
            !isEnabled -> AppTheme.color.disable
            isNegative -> AppTheme.color.redAccent
            else -> AppTheme.color.primary
        }
    )

    Row(
        modifier = modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick,
                enabled = isEnabled && !isLoading,
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = title,
            color = contentColor,
            style = AppTheme.textStyle.label.large,
        )

        val animatedPadding by animateDpAsState(
            if (isLoading) 8.dp else 0.dp,
            tween(durationMillis = 500)
        )
        val animatedLoadingSize by animateDpAsState(
            if (isLoading) 20.dp else 0.dp,
            tween(durationMillis = 500)
        )

        AnimatedVisibility(
            visible = isLoading && isEnabled,
            modifier = Modifier
                .padding(start = animatedPadding)
                .size(animatedLoadingSize),
        ) {
            LoadingIndicator(tint = contentColor)
        }
    }
}

@ThemeAndLocalePreviews
@Composable
private fun TextButtonPreview() {
    AflamiTheme {
        TextButton(
            title = "Button",
            onClick = {},
            isLoading = false,
            isEnabled = true,
            isNegative = false
        )
    }
}