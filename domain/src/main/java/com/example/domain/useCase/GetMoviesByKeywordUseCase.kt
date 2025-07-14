package com.example.domain.useCase

import com.example.domain.exceptions.NoSearchByKeywordResultFoundException
import com.example.domain.repository.MovieRepository
import com.example.domain.useCase.genreTypes.MovieGenre
import com.example.domain.utils.mapToGenreId
import com.example.entity.Movie

class GetMoviesByKeywordUseCase(
    private val movieRepository: MovieRepository
) {

    suspend operator fun invoke(
        keyword: String,
        rating: Float = 0f,
        movieGenre: MovieGenre = MovieGenre.ALL
    ): List<Movie> {

        return movieRepository
            .getMoviesByKeyword(keyword = keyword)
            .filter { movie -> movie.rating >= rating }
            .filter { movie ->
                if (movieGenre == MovieGenre.ALL)
                    return@filter true
                movie.categories.any { it.id == movieGenre.mapToGenreId() }
            }
            .sortedByDescending { it.popularity }
            .ifEmpty { throw NoSearchByKeywordResultFoundException() }

    }
}

