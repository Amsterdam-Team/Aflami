package com.amsterdam.repository.repository

import com.amsterdam.domain.repository.GameRepository
import com.amsterdam.entity.Movie
import com.amsterdam.entity.Character
import com.amsterdam.repository.datasource.local.GameLocalDataSource
import com.amsterdam.repository.datasource.local.GameSessionLocalDataSource
import com.amsterdam.repository.datasource.remote.MovieRemoteDataSource
import com.amsterdam.repository.datasource.remote.CharacterRemoteDataSource
import com.amsterdam.repository.mapper.toEntityList
import com.amsterdam.repository.mapper.toMovieEntityList
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GameRepositoryImpl @Inject constructor(
    private val gameLocalDataSource: GameLocalDataSource,
    private val gameSessionLocalDataSource : GameSessionLocalDataSource,
    private val movieRemoteDataSource: MovieRemoteDataSource,
    private val characterRemoteDataSource: CharacterRemoteDataSource
) : GameRepository {

    override suspend fun getRandomMoviesWithReleaseDate(requiredMoviesNumber: Int): List<Movie> {
        return movieRemoteDataSource.getRandomMoviesWithReleaseDate(requiredMoviesNumber)
            .toMovieEntityList()
    }

    override suspend fun getRandomMoviesWithPoster(requiredMoviesNumber: Int): List<Movie> {
        return movieRemoteDataSource.getRandomMoviesWithPoster(requiredMoviesNumber)
            .toMovieEntityList()
    }

    override suspend fun updatePoints(points: Int) {
        gameLocalDataSource.upsertPoints(points)
    }

    override fun getUserPoints(): Flow<Int> {
        return gameLocalDataSource.getUserPoints()
    }

    override suspend fun getCharacterDataQuestions(
        requiredNumber: Int,
    ): List<Character> {
        return characterRemoteDataSource.getRandomizedTrendingCharacter(requiredNumber = requiredNumber)
            .toEntityList()
    }

    override fun addOneSecondToGameTime(gameSessionId: Long) {
        gameSessionLocalDataSource.addOneSecond(gameSessionId)
    }

    override fun getTotalSpentSeconds(gameSessionId: Long) : Int {
       return gameSessionLocalDataSource.getTotalSpentSeconds(gameSessionId)
    }

    override fun addPointsToGame(points: Int, gameSessionId: Long) {
        gameSessionLocalDataSource.addPoints(points,gameSessionId)
    }

    override fun getCollectedPoints(gameSessionId: Long) : Int {
       return gameSessionLocalDataSource.getCollectedPoints(gameSessionId)
    }
}