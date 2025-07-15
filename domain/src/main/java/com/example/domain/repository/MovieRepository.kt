package com.example.domain.repository

import androidx.paging.PagingData
import com.example.domain.useCase.genreTypes.MovieGenre
import com.example.entity.Movie
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    suspend fun getMoviesByKeyword(
        keyword: String,
        rating: Float = 0f,
        movieGenre: MovieGenre = MovieGenre.ALL
    ): List<Movie>

    suspend fun getMoviesByActor(actorName: String): List<Movie>
    suspend fun getMoviesByCountryIsoCode(countryIsoCode: String): List<Movie>
    fun getMoviesPagingByKeyword(
        keyword: String,
        rating: Float,
        movieGenre: MovieGenre
    ): Flow<PagingData<Movie>>
}
