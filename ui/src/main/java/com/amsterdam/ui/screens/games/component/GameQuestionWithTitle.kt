package com.amsterdam.ui.screens.games.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun GameQuestionWithTitle(
    question: String,
    answers: List<String>,
    earnedPoint :Int?,
    selectedAnswerIndex: Int?,
    isAnswerCorrect: Boolean?,
    isHintEnabled: Boolean,
    modifier: Modifier = Modifier,
    isChoicesEnabled: Boolean = true,
    onHintClick: () -> Unit = {},
    onSelectAnswer: (Int) -> Unit = {},
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        GuessTitle(
            title = question,
            hintPoints = 10,
            isHintVisible = isHintEnabled,
            onClick = onHintClick,
            earnedPoint = earnedPoint
        )

        AdaptiveAnswersColumn(
            answers,
            selectedAnswerIndex,
            isAnswerCorrect,
            isChoicesEnabled,
            onSelectAnswer
        )
    }
}