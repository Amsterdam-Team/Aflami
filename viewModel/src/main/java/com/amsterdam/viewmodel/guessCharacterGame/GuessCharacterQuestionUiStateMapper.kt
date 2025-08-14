package com.amsterdam.viewmodel.guessCharacterGame

import com.amsterdam.domain.useCase.game.character.GenerateCharacterQuestionsUseCase.CharacterDataQuestion
import com.amsterdam.viewmodel.guessCharacterGame.GuessCharacterUiState.CharacterQuestionUiState

fun CharacterDataQuestion.toQuestionUiState(): CharacterQuestionUiState {
    return CharacterQuestionUiState(
        characterImageUrl = this.questionAsPosterUrl,
        characterChoices = this.choices,
        correctAnswer = this.correctAnswer,
        questionTimeSeconds = this.questionTimeSeconds
    )
}

fun CharacterQuestionUiState.toCharacterDataQuestion(): CharacterDataQuestion {
    return CharacterDataQuestion(
        questionAsPosterUrl = this.characterImageUrl,
        choices = this.characterChoices,
        correctAnswer = this.correctAnswer,
        questionTimeSeconds = this.questionTimeSeconds
    )
}

fun List<CharacterDataQuestion>.toQuestionsUiState(): List<CharacterQuestionUiState> =
    map { it.toQuestionUiState() }