package com.amsterdam.viewmodel.letsPlay

import com.amsterdam.entity.Game

data class LetsPlayUiState(
    val difficulties: List<GameDifficultyUiState> = emptyList(),
    val games: List<GameUiState> = emptyList(),
    val selectedGameType: Game.GameType? = null,
    val selectedDifficultyLevel: GameDifficultyUiState? = null,
    val isStartGameButtonEnable: Boolean = false,
    val totalUserPoint: Int = 0
) {

    data class GameDifficultyUiState(
        val totalQuestions: Int,
        val timeLimitSeconds: Int,
        val pointsPerQuestion: Int,
        val difficultyLevel: DifficultyLevelUiState
    ) {
        enum class DifficultyLevelUiState() {
            EASY,
            MEDIUM,
            HARD
        }
    }

    data class GameUiState(val gameType: Game.GameType, val requiredPoints: Int)
}