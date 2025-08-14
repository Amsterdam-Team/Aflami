package com.amsterdam.localdatasource.roomDataBase.datasource

import androidx.room.Transaction
import com.amsterdam.localdatasource.roomDataBase.daos.MovieCategoryInterestDao
import com.amsterdam.localdatasource.roomDataBase.daos.MovieDao
import com.amsterdam.repository.datasource.local.MovieLocalDataSource
import com.amsterdam.repository.dto.local.MovieLocalDto
import com.amsterdam.repository.dto.local.MovieCategoryCrossRefDto
import com.amsterdam.repository.dto.local.PopularMovieDto
import com.amsterdam.repository.dto.local.TopRatedMovieDto
import com.amsterdam.repository.dto.local.UpcomingMovieDto
import com.amsterdam.repository.dto.local.relation.MovieWithCategories
import kotlinx.datetime.Instant
import javax.inject.Inject

class MovieLocalDataDataSourceImpl @Inject constructor(
    private val movieDao: MovieDao,
    private val interestDao: MovieCategoryInterestDao
) : MovieLocalDataSource {
    override suspend fun getMovieById(
        movieId: Long,
        storedLanguage: String
    ): MovieLocalDto? {
        return movieDao.getMovieById(movieId, storedLanguage)
    }

    @Transaction
    override suspend fun upsertMovieWithCategories(
        movie: MovieLocalDto,
        categoryIds: List<Long>,
        storedLanguage: String
    ) {
        movieDao.upsertMovie(movie)
        val movieCrossRefs = categoryIds.map { categoryId ->
            MovieCategoryCrossRefDto(
                movieId = movie.movieId,
                categoryId = categoryId,
                storedLanguage = storedLanguage
            )
        }
        movieDao.upsertMovieCategoryCrossRefs(movieCrossRefs)
    }

    override suspend fun incrementGenreInterest(categoryId: Long) {
        interestDao.incrementInterest(categoryId)
    }

    override suspend fun upsertMovie(movie: MovieLocalDto) {
        movieDao.upsertMovie(movie)
    }

    override suspend fun getPopularMovies(storedLanguage: String): List<MovieWithCategories> {
        return movieDao.getPopularMovies(storedLanguage)
    }

    override suspend fun getUpcomingMovies(storedLanguage: String): List<MovieWithCategories> {
        return movieDao.getUpcomingMovies(storedLanguage)
    }

    override suspend fun getTopRatedMovies(storedLanguage: String): List<MovieLocalDto> {
        return movieDao.getTopRatedMovies(storedLanguage)
    }

    @Transaction
    override suspend fun upsertPopularMovies(movies: List<MovieLocalDto>) {
        movieDao.upsertMovies(movies)
        val entries = movies.map { movie ->
            PopularMovieDto(
                movieId = movie.movieId,
                storedLanguage = movie.storedLanguage
            )
        }
        movieDao.upsertPopularMovies(entries)
    }

    override suspend fun deleteExpiredPopularMovies(
        expirationTime: Instant,
        storedLanguage: String
    ) {
        movieDao.deleteExpiredPopularMovies(expirationTime, storedLanguage)
    }

    @Transaction
    override suspend fun upsertTopRatedMovies(movies: List<MovieLocalDto>) {
        movieDao.upsertMovies(movies)
        val entries = movies.map { movie ->
            TopRatedMovieDto(
                movieId = movie.movieId,
                storedLanguage = movie.storedLanguage
            )
        }
        movieDao.upsertTopRatedMovies(entries)
    }

    override suspend fun deleteAllExpiredTopRatedMovies(
        expirationTime: Instant,
        storedLanguage: String
    ) {
        movieDao.deleteAllExpiredTopRatedMovies(expirationTime, storedLanguage)
    }

    @Transaction
    override suspend fun upsertUpcomingMovies(movies: List<MovieLocalDto>) {
        movieDao.upsertMovies(movies)
        val entries = movies.map { movie ->
            UpcomingMovieDto(
                movieId = movie.movieId,
                storedLanguage = movie.storedLanguage
            )
        }
        movieDao.upsertUpcomingMovies(entries)
    }

    override suspend fun deleteExpiredUpcomingMovies(
        expirationTime: Instant,
        storedLanguage: String
    ) {
        movieDao.deleteExpiredUpcomingMovies(expirationTime, storedLanguage)
    }
}
