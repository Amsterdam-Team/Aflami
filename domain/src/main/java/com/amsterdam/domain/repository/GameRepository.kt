package com.amsterdam.domain.repository

import com.amsterdam.entity.Movie

interface GameRepository {
    suspend fun getTotalUserPoints(): Int
    suspend fun getRandomMoviesWithNotNullDate(requiredMoviesNumber: Int): List<Movie>
    suspend fun getPosterGuessingGameQuestions(questionCount: Int): List<PosterGuessingGameQuestion>
}
data class PosterGuessingGameQuestion(
    val posterUrl: String,
    val movieChoices: List<String>,
    val correctMovieName: String
)