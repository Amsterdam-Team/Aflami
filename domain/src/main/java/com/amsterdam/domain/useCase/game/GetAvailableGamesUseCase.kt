package com.amsterdam.domain.useCase.game

import com.amsterdam.entity.Game
import com.amsterdam.entity.GameDifficulty
import com.amsterdam.entity.GameDifficulty.DifficultyType


class GetAvailableGamesUseCase {
    operator fun invoke(): AvailableGames {
        val defaultLevels = listOf(
            GameDifficulty(
                totalQuestions = 5,
                timeLimitSeconds = 45,
                pointsPerQuestion = 5,
                difficultyType = DifficultyType.EASY
            ),
            GameDifficulty(
                totalQuestions = 10,
                timeLimitSeconds = 30,
                pointsPerQuestion = 10,
                difficultyType = DifficultyType.MEDIUM
            ),
            GameDifficulty(
                totalQuestions = 20,
                timeLimitSeconds = 10,
                pointsPerQuestion = 20,
                difficultyType = DifficultyType.HARD
            )
        )

        val games = listOf(
            Game(gameType = Game.GameType.GUESS_CHARACTER, requiredPoints = 0),
            Game(gameType = Game.GameType.GUESS_MOVIE_BY_POSTER, requiredPoints = 0),
            Game(gameType = Game.GameType.GUESS_RELEASE_YEAR, requiredPoints = 400),
            Game(gameType = Game.GameType.GUESS_GENRE, requiredPoints = 400)
        )
        return AvailableGames(games, defaultLevels)
    }

    data class AvailableGames(
        val games: List<Game>,
        val difficultyLevels: List<GameDifficulty>
    )
}