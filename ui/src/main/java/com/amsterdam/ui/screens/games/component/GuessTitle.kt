package com.amsterdam.ui.screens.games.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.amsterdam.designsystem.components.Text
import com.amsterdam.designsystem.theme.AflamiTheme
import com.amsterdam.designsystem.theme.AppTheme
import com.amsterdam.designsystem.utils.ThemeAndLocalePreviews

@Composable
fun GuessTitle(
    title: String,
    hintPoints: Int,
    earnedPoint: Int?,
    isHintVisible: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    GuessCard(
        points = hintPoints,
        modifier = modifier,
        onClick = onClick,
        isHintVisible = isHintVisible,
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                modifier =
                    Modifier
                        .align(Alignment.Center)
                        .padding(vertical = 65.dp),
                text = title,
                style = AppTheme.textStyle.title.large,
                color = AppTheme.color.title,
                textAlign = TextAlign.Center
            )
            AnimatedVisibility(
                visible = earnedPoint != null && earnedPoint != 0,
                enter = expandIn(
                    expandFrom = Alignment.TopCenter,
                    clip = false,
                    animationSpec = tween(1000)
                ) + fadeIn(animationSpec = tween(1000))
            ) {
                if (earnedPoint == null) return@AnimatedVisibility
                GameScoreCircle(
                    score = earnedPoint,
                    modifier = Modifier.padding(bottom = if (isHintVisible) 24.dp else 0.dp)
                )
            }
        }
    }
}

@ThemeAndLocalePreviews
@Composable
private fun GuessTitleHintVisiblePreview() {
    AflamiTheme {
        GuessTitle(
            title = "The Green Mile",
            hintPoints = 10,
            isHintVisible = true,
            onClick = {},
            earnedPoint = 20,
        )
    }
}

@ThemeAndLocalePreviews
@Composable
private fun GuessTitleHintNotVisiblePreview() {
    AflamiTheme {
        GuessTitle(
            title = "The Green Mile",
            hintPoints = 10,
            isHintVisible = false,
            onClick = {},
            earnedPoint = 20,
        )
    }
}
