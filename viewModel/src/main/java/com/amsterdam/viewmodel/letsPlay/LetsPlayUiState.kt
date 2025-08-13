package com.amsterdam.viewmodel.letsPlay

import com.amsterdam.entity.Game
import com.amsterdam.viewmodel.letsPlay.LetsPlayUiState.GameUiState.GameTypeUiState

data class LetsPlayUiState(
    val difficulties: List<GameDifficultyUiState> = emptyList(),
    val games: List<GameUiState> = emptyList(),
    val selectedGameTypeUiState: GameTypeUiState? = null,
    val selectedDifficultyLevel: GameDifficultyUiState? = null,
    val isStartGameButtonEnable: Boolean = false,
    val totalUserPoint: Int = 550
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

fun GameTypeUiState.toGameType(): Game.GameType {
    return when (this) {
        GameTypeUiState.GUESS_CHARACTER -> Game.GameType.GUESS_CHARACTER
        GameTypeUiState.GUESS_MOVIE_BY_POSTER -> Game.GameType.GUESS_MOVIE_BY_POSTER
        GameTypeUiState.GUESS_MOVIE_BY_RELEASE -> Game.GameType.GUESS_MOVIE_BY_RELEASE
        GameTypeUiState.GUESS_MOVIE_BY_GENRE -> Game.GameType.GUESS_MOVIE_BY_GENRE
    }
}