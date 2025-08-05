package com.amsterdam.localdatasource.roomDataBase.datasource

import androidx.room.Transaction
import com.amsterdam.localdatasource.roomDataBase.daos.MovieCategoryInterestDao
import com.amsterdam.localdatasource.roomDataBase.daos.MovieDao
import com.amsterdam.repository.datasource.local.MovieLocalSource
import com.amsterdam.repository.dto.local.LocalMovieDto
import com.amsterdam.repository.dto.local.MovieCategoryCrossRefDto
import com.amsterdam.repository.dto.local.PopularMovieDto
import com.amsterdam.repository.dto.local.TopRatedMovieDto
import com.amsterdam.repository.dto.local.UpcomingMovieDto
import com.amsterdam.repository.dto.local.relation.MovieWithCategories
import kotlinx.datetime.Instant
import javax.inject.Inject

class MovieLocalDataSourceImpl @Inject constructor(
    private val movieDao: MovieDao,
    private val interestDao: MovieCategoryInterestDao
) : MovieLocalSource {
    override suspend fun getMovieById(
        movieId: Long,
        storedLanguage: String
    ): LocalMovieDto? {
        return movieDao.getMovieById(movieId, storedLanguage)
    }

    @Transaction
    override suspend fun addMovieWithCategories(
        movie: LocalMovieDto,
        categoryIds: List<Long>,
        storedLanguage: String
    ) {
        movieDao.insertMovie(movie)
        val movieCrossRefs = categoryIds.map { categoryId ->
            MovieCategoryCrossRefDto(
                movieId = movie.movieId,
                categoryId = categoryId,
                storedLanguage = storedLanguage
            )
        }
        movieDao.insertMovieCategoryCrossRefs(movieCrossRefs)
    }

    override suspend fun incrementGenreInterest(categoryId: Long) {
        interestDao.incrementInterest(categoryId)
    }

    override suspend fun insertMovie(movie: LocalMovieDto) {
        movieDao.insertMovie(movie)
    }

    override suspend fun getPopularMovies(storedLanguage: String): List<MovieWithCategories> {
        return movieDao.getPopularMovies(storedLanguage)
    }

    override suspend fun getUpcomingMovies(storedLanguage: String): List<MovieWithCategories> {
        return movieDao.getUpcomingMovies(storedLanguage)
    }

    override suspend fun getTopRatedMovies(storedLanguage: String): List<LocalMovieDto> {
        return movieDao.getTopRatedMovies(storedLanguage)
    }

    @Transaction
    override suspend fun addPopularMovies(movies: List<LocalMovieDto>) {
        movieDao.insertMovies(movies)
        val entries = movies.map { movie ->
            PopularMovieDto(
                movieId = movie.movieId,
                storedLanguage = movie.storedLanguage
            )
        }
        movieDao.insertPopularMovies(entries)
    }

    override suspend fun deleteExpiredPopularMovies(
        expirationTime: Instant,
        storedLanguage: String
    ) {
        movieDao.deleteExpiredPopularMovies(expirationTime, storedLanguage)
    }

    @Transaction
    override suspend fun addTopRatedMovies(movies: List<LocalMovieDto>) {
        movieDao.insertMovies(movies)
        val entries = movies.map { movie ->
            TopRatedMovieDto(
                movieId = movie.movieId,
                storedLanguage = movie.storedLanguage
            )
        }
        movieDao.insertTopRatedMovies(entries)
    }

    override suspend fun deleteAllExpiredTopRatedMovies(
        expirationTime: Instant,
        storedLanguage: String
    ) {
        movieDao.deleteAllExpiredTopRatedMovies(expirationTime, storedLanguage)
    }

    @Transaction
    override suspend fun addUpcomingMovies(movies: List<LocalMovieDto>) {
        movieDao.insertMovies(movies)
        val entries = movies.map { movie ->
            UpcomingMovieDto(
                movieId = movie.movieId,
                storedLanguage = movie.storedLanguage
            )
        }
        movieDao.insertUpcomingMovies(entries)
    }

    override suspend fun deleteExpiredUpcomingMovies(
        expirationTime: Instant,
        storedLanguage: String
    ) {
        movieDao.deleteExpiredUpcomingMovies(expirationTime, storedLanguage)
    }
}
