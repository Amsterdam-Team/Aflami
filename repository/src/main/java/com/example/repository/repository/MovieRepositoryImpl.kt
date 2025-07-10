package com.example.repository.repository

import com.example.domain.repository.MovieRepository
import com.example.entity.Movie
import com.example.repository.dataSource.local.LocalCategoryDataSource
import com.example.repository.dataSource.local.LocalMovieDataSource

class MovieRepositoryImpl(
    private val localMovieDataSource: LocalMovieDataSource,
    private val localCategoryDataSource: LocalCategoryDataSource
) :
    MovieRepository {
    override suspend fun getMoviesByKeyword(keyword: String): List<Movie> {
        TODO("Not yet implemented")
    }

    override suspend fun getMoviesByActor(actorName: String): List<Movie> {
        TODO("Not yet implemented")
    }

    override suspend fun getMoviesByCountryIsoCode(countryIsoCode: String): List<Movie> {
        TODO("Not yet implemented")
    }
}