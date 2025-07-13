package com.example.domain.useCase

import com.example.domain.exceptions.NoSearchByKeywordResultFoundException
import com.example.domain.repository.MovieRepository
import com.example.domain.useCase.genreTypes.MovieGenre
import com.example.entity.Movie

class GetMoviesByKeywordUseCase(
    private val movieRepository: MovieRepository
) {

    suspend operator fun invoke(
        keyword: String,
        rating: Float = 0f,
        movieGenre: MovieGenre = MovieGenre.ALL
    ): List<Movie> {
        return movieRepository.getMoviesByKeyword(
            keyword = keyword,
            rating = rating,
            movieGenre = movieGenre
        ).sortedByDescending { it.popularity }
            .ifEmpty { throw NoSearchByKeywordResultFoundException() }

    }
}

