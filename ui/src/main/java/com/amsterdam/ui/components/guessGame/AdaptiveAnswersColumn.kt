package com.amsterdam.ui.components.guessGame

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.amsterdam.ui.components.selection.AnswerSelectionItem
import com.amsterdam.ui.components.selection.AnswerStatus

@Composable
fun AdaptiveAnswersColumn(
    answers: List<String>,
    selectedAnswerIndex: Int?,
    isAnswerCorrect: Boolean?,
    isChoiceEnabled: Boolean,
    onSelectAnswer: (Int) -> Unit
) {
    val itemWidth = 336.dp
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val itemsPerRow = maxOf(1, (screenWidth / itemWidth).toInt())

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        answers.chunked(itemsPerRow).forEach { rowAnswers ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                rowAnswers.forEach { answer ->
                    val index = answers.indexOf(answer)
                    val status =
                        if (selectedAnswerIndex == index) {
                            when (isAnswerCorrect) {
                                true -> AnswerStatus.Correct
                                false -> AnswerStatus.Wrong
                                null -> AnswerStatus.Unselected
                            }
                        } else {
                            AnswerStatus.Unselected
                        }

                    AnswerSelectionItem(
                        text = answer,
                        status = status,
                        onClick = {
                            if (isChoiceEnabled) return@AnswerSelectionItem
                            onSelectAnswer(index)
                        },
                        modifier = Modifier.width(screenWidth / itemsPerRow)
                    )
                }
            }
        }
    }
}