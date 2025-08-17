package com.amsterdam.ui.screens.games.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun GameQuestionWithImage(
    question: String,
    blurRadius: Int,
    answers: List<String>,
    selectedAnswerIndex: Int?,
    isAnswerCorrect: Boolean?,
    isHintEnabled: Boolean,
    modifier: Modifier = Modifier,
    isChoicesEnabled: Boolean = true,
    onHintClick: () -> Unit = {},
    onSelectAnswer: (Int) -> Unit = {},
    earnedPoint: Int?,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        QuestionImageWithScore(
            questionImageModel = question,
            blurRadius = blurRadius,
            earnedPoint = earnedPoint,
            isHintEnabled = isHintEnabled,
            onHintClick = onHintClick,
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