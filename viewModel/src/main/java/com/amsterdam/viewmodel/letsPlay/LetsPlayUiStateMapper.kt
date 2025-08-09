package com.amsterdam.viewmodel.letsPlay

import com.amsterdam.domain.useCase.game.GetAvailableGamesUseCase.AvailableGames
import com.amsterdam.entity.Game
import com.amsterdam.entity.Game.GameType
import com.amsterdam.entity.GameDifficulty
import com.amsterdam.entity.GameDifficulty.DifficultyType
import com.amsterdam.viewmodel.letsPlay.LetsPlayUiState.GameDifficultyUiState
import com.amsterdam.viewmodel.letsPlay.LetsPlayUiState.GameDifficultyUiState.DifficultyLevelUiState
import com.amsterdam.viewmodel.letsPlay.LetsPlayUiState.GameUiState.GameTypeUiState

fun AvailableGames.toLetsPlayUiState() = LetsPlayUiState(
    difficulties = difficultyLevels.toGameDifficultiesUiState(),
    games = games.toGamesUiState()
)

fun List<Game>.toGamesUiState() = map { it.toGameUiState() }

fun Game.toGameUiState() = LetsPlayUiState.GameUiState(
    requiredPoints = requiredPoints,
    gameTypeUiState = gameType.toGameTypeUiState()
)

fun GameType.toGameTypeUiState(): GameTypeUiState {
    return when (this) {
        GameType.GUESS_CHARACTER -> GameTypeUiState.GUESS_CHARACTER
        GameType.GUESS_MOVIE_BY_POSTER -> GameTypeUiState.GUESS_MOVIE_BY_POSTER
        GameType.GUESS_MOVIE_BY_RELEASE -> GameTypeUiState.GUESS_MOVIE_BY_RELEASE
        GameType.GUESS_MOVIE_BY_GENRE -> GameTypeUiState.GUESS_MOVIE_BY_GENRE
    }
}

fun List<GameDifficulty>.toGameDifficultiesUiState() = map { it.toGameDifficultyUiState() }

fun GameDifficulty.toGameDifficultyUiState() = GameDifficultyUiState(
    totalQuestions = totalQuestions,
    timeLimitSeconds = timeLimitSeconds,
    pointsPerQuestion = pointsPerQuestion,
    difficultyLevel = difficultyType.toDifficultyTypeUiState(),
)

fun DifficultyType.toDifficultyTypeUiState(): DifficultyLevelUiState {
    return when (this) {
        DifficultyType.EASY -> DifficultyLevelUiState.EASY
        DifficultyType.MEDIUM -> DifficultyLevelUiState.MEDIUM
        DifficultyType.HARD -> DifficultyLevelUiState.HARD
    }
}