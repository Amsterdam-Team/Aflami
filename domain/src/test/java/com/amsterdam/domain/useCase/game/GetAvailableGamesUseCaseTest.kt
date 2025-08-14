package com.amsterdam.domain.useCase.game

import com.amsterdam.entity.Game
import com.amsterdam.entity.GameDifficulty
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class GetAvailableGamesUseCaseTest {
    private val getAvailableGamesUseCase by lazy { GetAvailableGamesUseCase() }

    @Test
    fun `should return available games and difficulty levels when called`() {
        val result = getAvailableGamesUseCase()

        assertThat(result).isEqualTo(expectedAvailableGames)
    }

    private val expectedAvailableGames = GetAvailableGamesUseCase.AvailableGames(
        games = listOf(
            Game(gameType = Game.GameType.GUESS_CHARACTER, requiredPoints = 0),
            Game(gameType = Game.GameType.GUESS_MOVIE_BY_POSTER, requiredPoints = 0),
            Game(gameType = Game.GameType.GUESS_MOVIE_BY_RELEASE, requiredPoints = 400),
            Game(gameType = Game.GameType.GUESS_MOVIE_BY_GENRE, requiredPoints = 400)
        ),
        difficultyLevels = listOf(
            GameDifficulty(5, 45, 5, GameDifficulty.DifficultyType.EASY),
            GameDifficulty(10, 30, 10, GameDifficulty.DifficultyType.MEDIUM),
            GameDifficulty(20, 10, 20, GameDifficulty.DifficultyType.HARD)
        )
    )
}