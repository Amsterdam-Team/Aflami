package com.amsterdam.designsystem.components.buttons

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.amsterdam.designsystem.theme.AflamiTheme
import com.amsterdam.designsystem.theme.AppTheme
import com.amsterdam.designsystem.utils.ThemeAndLocalePreviews
import com.amsterdam.designsystem.utils.ripple

enum class RadioState {
    Default,
    Selected,
    Unselected,
}

@Composable
fun RadioButton(
    state: RadioState,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    val (borderColor, borderWidth) =
        when (state) {
            RadioState.Default -> AppTheme.color.disable to 1.dp
            RadioState.Selected -> AppTheme.color.primary to 6.dp
            RadioState.Unselected -> AppTheme.color.disable to 6.dp
        }

    val color =
        when (state) {
            RadioState.Default -> AppTheme.color.surfaceHigh
            RadioState.Selected -> Color.Unspecified
            RadioState.Unselected -> Color.Unspecified
        }

    Box(
        modifier =
            modifier
                .size(18.dp)
                .clip(CircleShape)
                .background(color)
                .border(
                    width = borderWidth,
                    color = borderColor,
                    shape = CircleShape,
                )
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = ripple(),
                    onClick = onClick,
                    role = Role.RadioButton,
                ),
    )
}

@ThemeAndLocalePreviews
@Composable
private fun CustomRadioButtonPreview() {
    AflamiTheme {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            RadioButton(
                modifier = Modifier.padding(bottom = 16.dp),
                state = RadioState.Default,
            )
            RadioButton(
                modifier = Modifier.padding(bottom = 16.dp),
                state = RadioState.Selected,
            )
            RadioButton(
                state = RadioState.Unselected,
            )
        }
    }
}
