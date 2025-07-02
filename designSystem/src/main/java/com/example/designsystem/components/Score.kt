package com.example.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.designsystem.theme.AflamiTheme
import com.example.designsystem.theme.AppTheme
import com.example.designsystem.utils.ThemeAndLocalePreviews

@Composable
fun Score(
    text: String,
    textColor: Color,
    positive: Boolean,
    backgroundColor: Color,
    modifier: Modifier = Modifier
) {
    val sign = if (positive) "+" else "-"
    Text(
        text = sign + text,
        style = AppTheme.textStyle.label.large,
        modifier = modifier
            .background(shape = CircleShape, color = backgroundColor)
            .padding(horizontal = 14.5.dp, vertical = 10.dp),
        color = textColor,
    )
}

@Composable
@ThemeAndLocalePreviews
private fun ScorePreview() {
    AflamiTheme {
        Column {
            Score(
                text = 1.toString(),
                textColor = AppTheme.color.greenAccent,
                backgroundColor = AppTheme.color.greenVariant,
                positive = true,
            )
            Score(
                text = 1.toString(),
                textColor = AppTheme.color.redAccent,
                backgroundColor = AppTheme.color.redVariant,
                positive = false,
            )
        }
    }
}
