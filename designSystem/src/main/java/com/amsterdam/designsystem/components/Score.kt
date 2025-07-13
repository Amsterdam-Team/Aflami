package com.amsterdam.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.amsterdam.designsystem.theme.AflamiTheme
import com.amsterdam.designsystem.theme.AppTheme
import com.amsterdam.designsystem.utils.ThemeAndLocalePreviews

@Composable
fun Score(
    score: Int,
    modifier: Modifier = Modifier
) {
    val isNegative = score < 0
    val sign = if (score > 0) "+" else ""
    val textColor = if (isNegative) AppTheme.color.redAccent else AppTheme.color.greenAccent
    val backgroundColor = if (isNegative) AppTheme.color.redVariant else AppTheme.color.greenVariant

    Box(
        modifier = modifier
            .background(shape = CircleShape, color = backgroundColor)
            .size(44.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "$sign$score",
            style = AppTheme.textStyle.label.large,
            color = textColor,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
@ThemeAndLocalePreviews
private fun ScorePreview() {
    AflamiTheme {
        Column {
            Score(score = -1)
            Score(score = 1)
            Score(score = 0)
        }
    }
}
