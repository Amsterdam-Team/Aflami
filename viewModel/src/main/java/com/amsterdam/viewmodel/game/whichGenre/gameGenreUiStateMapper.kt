package com.amsterdam.viewmodel.game.whichGenre

import com.amsterdam.domain.useCase.game.whichGenre.GenerateMovieGenreQuestionsUseCase.MovieGenreQuestion

fun GameQuestionUiState.toQuestion() = MovieGenreQuestion(
    id = id,
    question = questionData,
    genreChoices = answers,
    correctChoice = correctAnswer,
    questionTime = questionTime
)

fun MovieGenreQuestion.toUiState() = GameQuestionUiState(
    id = id,
    questionData = question,
    answers = genreChoices,
    correctAnswer = correctChoice,
    questionTime = questionTime
)