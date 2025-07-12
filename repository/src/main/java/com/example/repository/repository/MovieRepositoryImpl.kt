package com.example.repository.repository

import com.example.domain.repository.MovieRepository
import com.example.entity.Movie
import com.example.repository.datasource.local.LocalMovieDataSource
import com.example.repository.datasource.remote.RemoteMovieDatasource
import com.example.repository.mapper.local.MovieLocalMapper
import com.example.repository.mapper.remote.RemoteMovieMapper

class MovieRepositoryImpl(
    private val localMovieDataSource: LocalMovieDataSource,
    private val remoteMovieDatasource: RemoteMovieDatasource,
    private val remoteMovieMapper: RemoteMovieMapper,
    private val localMovieMapper: MovieLocalMapper,
) : MovieRepository {
    override suspend fun getMoviesByKeyword(keyword: String): List<Movie> {
        TODO("Not yet implemented")
    }

    override suspend fun getMoviesByActor(actorName: String): List<Movie> {
        TODO("Not yet implemented")
    }

    override suspend fun getMoviesByCountryIsoCode(countryIsoCode: String): List<Movie> {
        val remoteMovies = remoteMovieDatasource
            .getMoviesByCountryIsoCode(countryIsoCode)
            .results
            .map { remoteMovieMapper.mapToDomain(it) }
        return remoteMovies
    }
}