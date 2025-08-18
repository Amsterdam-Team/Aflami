package com.amsterdam.domain.useCase.mood

import com.amsterdam.domain.repository.MovieRepository
import com.amsterdam.entity.Movie
import com.amsterdam.entity.category.MovieGenre

class GetMoviesByMoodUseCase(
    private val movieRepository: MovieRepository
) {
    suspend operator fun invoke(mood: Mood): List<Movie> {
        val randomPage = (1..MAX_PAGES).random()
        return movieRepository.getMoviesByGenres(mood.movieGenres, randomPage)
    }

    enum class Mood(val movieGenres: List<MovieGenre>) {
        SAD(listOf(MovieGenre.COMEDY)),
        NEUTRAL(listOf(MovieGenre.DOCUMENTARY, MovieGenre.MYSTERY)),
        ROMANTIC(listOf(MovieGenre.ROMANCE, MovieGenre.MUSIC)),
        ANGRY(listOf(MovieGenre.COMEDY, MovieGenre.ANIMATION, MovieGenre.FAMILY)),
        DEPRESSED(listOf(MovieGenre.DRAMA, MovieGenre.ANIMATION)),
        SAD_DIZZY(listOf(MovieGenre.ADVENTURE, MovieGenre.FANTASY, MovieGenre.SCIENCE_FICTION));
    }

    companion object {
        const val MAX_PAGES = 100
    }
}