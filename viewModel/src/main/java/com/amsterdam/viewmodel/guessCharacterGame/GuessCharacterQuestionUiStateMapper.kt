package com.amsterdam.viewmodel.guessCharacterGame

import com.amsterdam.domain.utils.GameQuestion
import com.amsterdam.viewmodel.guessCharacterGame.GuessCharacterUiState.CharacterQuestionUiState

fun GameQuestion<String>.toQuestionUiState(): CharacterQuestionUiState {
    return CharacterQuestionUiState(
        characterImageUrl = this.question,
        characterChoices = this.choices,
        correctAnswer = this.correctChoice,
        questionTimeSeconds = this.questionTime
    )
}

fun CharacterQuestionUiState.toCharacterDataQuestion(): GameQuestion<String> {
    return GameQuestion(
        question = this.characterImageUrl,
        choices = this.characterChoices,
        correctChoice = this.correctAnswer,
        questionTime = this.questionTimeSeconds
    )
}

fun List<GameQuestion<String>>.toQuestionsUiState(): List<CharacterQuestionUiState> =
    map { it.toQuestionUiState() }