package com.amsterdam.domain.repository

import com.amsterdam.entity.Movie
import com.amsterdam.entity.Character
import kotlinx.coroutines.flow.Flow


interface GameRepository {
    suspend fun getRandomMoviesWithReleaseDate(requiredMoviesNumber: Int): List<Movie>
    suspend fun getRandomMoviesWithPoster(requiredMoviesNumber: Int): List<Movie>
    suspend fun updatePoints(points: Int)
    fun getUserPoints(): Flow<Int>
    suspend fun getCharacterDataQuestions(requiredNumber: Int): List<Character>
    fun addOneSecondToGameTime(gameSessionId: Long)
    fun getTotalSpentSeconds(gameSessionId: Long): Int
    fun addPointsToGame(points: Int, gameSessionId: Long)
    fun getCollectedPoints(gameSessionId: Long): Int
}