package com.amsterdam.repository.repository

import com.amsterdam.domain.repository.GameRepository
import com.amsterdam.domain.useCase.game.PosterGuessingGameQuestion
import com.amsterdam.entity.Movie
import com.amsterdam.repository.datasource.remote.MovieRemoteSource
import com.amsterdam.repository.mapper.remote.toMovieEntityList
import javax.inject.Inject

class GameRepositoryImpl @Inject constructor(
    //   private val gameLocalDataSource: GameLocalDataSource,
    private val movieRemoteSource: MovieRemoteSource
) : GameRepository {

    override suspend fun getTotalUserPoints(): Int {
        return 100
    }

    override suspend fun getRandomMoviesWithNotNullDate(requiredMoviesNumber: Int): List<Movie> {
        val totalPages = getTotalPagesForPopularMovies()
        val collectedMovies = mutableListOf<Movie>()
        val usedPages = mutableSetOf<Int>()

        while (collectedMovies.size < requiredMoviesNumber && usedPages.size < totalPages) {
            val randomPage = (1..totalPages).random().also { usedPages.add(it) }
            val pageMovies = getPopularMoviesByPage(randomPage)
                .filter { it.releaseDate != null }

            for (movie in pageMovies) {
                if (!collectedMovies.contains(movie)) {
                    collectedMovies.add(movie)
                    if (collectedMovies.size == requiredMoviesNumber) break
                }
            }
        }

        return collectedMovies
    }

    override suspend fun getPosterGuessingGameQuestions(questionCount: Int): List<PosterGuessingGameQuestion> {
        val totalPages = getTotalPagesForPopularMovies()
        val randomPage = (1..totalPages).random()
        val movies = movieRemoteSource.getPopularMovies(page = randomPage).results.toMovieEntityList()
        val questions = mutableListOf<PosterGuessingGameQuestion>()
        val usedMovies = mutableSetOf<Movie>()

        while (questions.size < questionCount) {
            val correctMovie = movies.filter { !usedMovies.contains(it) }.randomOrNull() ?: break
            usedMovies.add(correctMovie)
            val incorrectChoices = movies.filter { it != correctMovie }
                .shuffled()
                .take(3)
                .map { it.name }

            val choices = (incorrectChoices + correctMovie.name).shuffled()
            val question = PosterGuessingGameQuestion(
                posterUrl = correctMovie.posterUrl,
                movieChoices = choices,
                correctMovieName = correctMovie.name
            )
            questions.add(question)
        }

        return questions
    }

    private suspend fun getTotalPagesForPopularMovies(): Int {
        return movieRemoteSource.getPopularMovies(page = 1).totalPages
    }

    private suspend fun getPopularMoviesByPage(page: Int): List<Movie> {
        return movieRemoteSource.getPopularMovies(page = page).results.toMovieEntityList()
    }
}