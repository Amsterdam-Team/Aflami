package com.amsterdam.viewmodel.guessWhichGenre

import com.amsterdam.domain.utils.GameQuestion
import com.amsterdam.entity.category.MovieGenre

fun GameQuestionUiState.toQuestion() = GameQuestion(
    question = questionData,
    choices = answers,
    correctChoice = correctAnswer,
    questionTime = questionTime
)

fun GameQuestion<MovieGenre>.toUiState() = GameQuestionUiState(
    questionData = question,
    answers = choices,
    correctAnswer = correctChoice,
    questionTime = questionTime
)