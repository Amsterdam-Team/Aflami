package com.example.designsystem.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.example.designsystem.theme.AflamiTheme
import com.example.designsystem.theme.AppTheme
import com.example.designsystem.utils.ThemeAndLocalePreviews

enum class RadioState {
    Default,
    Selected,
    Unselected
}

@Composable
fun CustomRadioButton(
    state: RadioState,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    val (borderColor, borderWidth) = when (state) {
        RadioState.Default -> AppTheme.color.disable to 1.dp
        RadioState.Selected -> AppTheme.color.primary to 6.dp
        RadioState.Unselected -> AppTheme.color.disable to 6.dp
    }

    Box(
        modifier = modifier
            .size(18.dp)
            .clip(CircleShape)
            .border(
                width = borderWidth,
                color = borderColor,
                shape = CircleShape
            )
            .clickable(
                onClick = onClick,
                role = Role.RadioButton
            )
    )
}
@ThemeAndLocalePreviews
@Composable
private fun CustomRadioButtonPreview() {
    AflamiTheme {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CustomRadioButton(
                modifier = Modifier.padding(bottom = 16.dp),
                state = RadioState.Default
            )
            CustomRadioButton(
                modifier = Modifier.padding(bottom = 16.dp),
                state = RadioState.Selected
            )
            CustomRadioButton(
                state = RadioState.Unselected
            )
        }
    }
}
