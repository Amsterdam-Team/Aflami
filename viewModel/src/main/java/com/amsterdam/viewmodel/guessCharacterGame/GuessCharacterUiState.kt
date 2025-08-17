package com.amsterdam.viewmodel.guessCharacterGame

import com.amsterdam.viewmodel.sharedGame.TimerUiState

data class GuessCharacterUiState(
    val isLoading: Boolean = true,
    val timerCounter: Int = 0,
    val gameSessionId : Long = 0,
    val totalCollectedPoints : Int = 0,
    val questions: List<CharacterQuestionUiState> = emptyList(),
    val selectedAnswerIndex: Int? = null,
    val isAnswerCorrect : Boolean? = null,
    val isHintEnabled : Boolean = true,
    val isNotEnoughPointsDialogVisible: Boolean = false,
    val isNextEnabled : Boolean = false,
    val questionsCounts: Int = 0,
    val currentQuestionIndex: Int = 0,
    val timerUiState : TimerUiState = TimerUiState()
) {
    data class CharacterQuestionUiState(
        val characterImageUrl: String = "",
        val characterChoices: List<String> = emptyList(),
        val correctAnswer : String = "",
        val questionTimeSeconds : Int = 0,
        val blurRadius: Int = 8
    )
}


