package com.amsterdam.viewmodel.letsPlay

import com.amsterdam.viewmodel.letsPlay.LetsPlayUiState.GameUiState.GameTypeUiState

data class LetsPlayUiState(
    val difficulties: List<GameDifficultyUiState> = emptyList(),
    val games: List<GameUiState> = emptyList(),
    val selectedGameTypeUiState: GameTypeUiState? = null,
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
    data class GameUiState(val gameTypeUiState: GameTypeUiState, val requiredPoints: Int) {
        enum class GameTypeUiState {
            GUESS_CHARACTER,
            GUESS_MOVIE_BY_POSTER,
            GUESS_MOVIE_BY_RELEASE,
            GUESS_MOVIE_BY_GENRE
        }
    }
}