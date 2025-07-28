package com.amsterdam.domain.useCase.home

import com.amsterdam.domain.repository.MovieRepository
import com.amsterdam.entity.Movie
import com.amsterdam.entity.category.MovieGenre

class GetUpcomingMoviesUseCase (private val movieRepository: MovieRepository) {
    suspend operator fun invoke(genre: MovieGenre): List<Movie> {
        val upcomingMovies = movieRepository.getUpcomingMovies()
        val filteredMovies = filterByGenre(upcomingMovies, genre)
        return sortByPopularityAndRating(filteredMovies)
    }

    private fun filterByGenre(movies: List<Movie>, genre: MovieGenre): List<Movie> {
        return if (genre == MovieGenre.ALL) movies else movies.filter { hasGenre(it, genre) }
    }

    private fun hasGenre(movie: Movie, genre: MovieGenre): Boolean {
        return movie.categories.isNotEmpty() && genre in movie.categories
    }

    private fun sortByPopularityAndRating(movies: List<Movie>): List<Movie> {
        return movies.sortedWith(
            compareByDescending<Movie> { it.popularity }
                .thenByDescending { it.rating }
        )
    }
}