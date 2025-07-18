package com.example.ui.components.guessGame

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.designsystem.components.Text
import com.example.designsystem.theme.AflamiTheme
import com.example.designsystem.theme.AppTheme
import com.example.designsystem.utils.ThemeAndLocalePreviews

@Composable
fun GuessTitle(
    title: String,
    points: Int,
    isHintVisible: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    GuessCard(
        points = points,
        modifier = modifier,
        onClick = onClick,
        isHintVisible = isHintVisible,
    ) {
        Text(
            modifier =
                Modifier
                    .align(Alignment.Center)
                    .padding(vertical = 65.dp),
            text = title,
            style = AppTheme.textStyle.title.large,
            color = AppTheme.color.title,
        )
    }
}

@ThemeAndLocalePreviews
@Composable
private fun GuessTitleHintVisiblePreview() {
    AflamiTheme {
        GuessTitle(
            title = "The Green Mile",
            points = 10,
            isHintVisible = true,
            onClick = {},
        )
    }
}

@ThemeAndLocalePreviews
@Composable
private fun GuessTitleHintNotVisiblePreview() {
    AflamiTheme {
        GuessTitle(
            title = "The Green Mile",
            points = 10,
            isHintVisible = false,
            onClick = {},
        )
    }
}
