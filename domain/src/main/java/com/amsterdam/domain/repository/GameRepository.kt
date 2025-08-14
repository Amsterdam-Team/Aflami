package com.amsterdam.domain.repository

import com.amsterdam.entity.Movie
import kotlinx.coroutines.flow.Flow
import com.amsterdam.entity.People


interface GameRepository {
    suspend fun getRandomMoviesWithNotNullDate(requiredMoviesNumber: Int): List<Movie>
    suspend fun getRandomMoviesWithNotNullPoster(requiredMoviesNumber: Int): List<Movie>
    suspend fun updatePoints(points: Int)
    fun getUserPoints(): Flow<Int>
    suspend fun getCharacterDataQuestions(requiredNumber: Int): List<People>
}