package com.amsterdam.localdatasource.roomDataBase.datasource

import androidx.room.Transaction
import com.amsterdam.localdatasource.roomDataBase.daos.MovieDao
import com.amsterdam.localdatasource.roomDataBase.daos.TopRatedMovieDao
import com.amsterdam.repository.datasource.local.TopRatedMovieLocalSource
import com.amsterdam.repository.dto.local.LocalMovieDto
import com.amsterdam.repository.dto.local.TopRatedMovieDto
import kotlinx.datetime.Instant
import javax.inject.Inject

class TopRatedMovieLocalSourceImpl @Inject constructor(
    private val movieDao: MovieDao,
    private val topRatedMovieDao: TopRatedMovieDao
) : TopRatedMovieLocalSource {

    @Transaction
    override suspend fun addTopRatedMovies(movies: List<LocalMovieDto>) {
        movieDao.insertMovies(movies)
        val entries = movies.map { movie ->
            TopRatedMovieDto(
                movieId = movie.movieId,
                storedLanguage = movie.storedLanguage
            )
        }
        topRatedMovieDao.insertTopRatedMovies(entries)
    }

    override suspend fun deleteAllExpiredTopRatedMovies(
        expirationTime: Instant,
        storedLanguage: String
    ) {
        topRatedMovieDao.deleteAllExpiredTopRatedMovies(expirationTime, storedLanguage)
    }
}