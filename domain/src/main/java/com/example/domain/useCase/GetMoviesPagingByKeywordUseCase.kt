package com.example.domain.useCase

import androidx.paging.PagingData
import com.example.domain.repository.MovieRepository
import com.example.domain.useCase.genreTypes.MovieGenre
import com.example.entity.Movie
import kotlinx.coroutines.flow.Flow

class GetMoviesPagingByKeywordUseCase(
    private val movieRepository: MovieRepository
) {
    operator fun invoke(
        keyword: String,
        rating: Float = 0f,
        movieGenre: MovieGenre = MovieGenre.ALL
    ): Flow<PagingData<Movie>> {
        return movieRepository.getMoviesPagingByKeyword(keyword, rating, movieGenre)
    }
}
