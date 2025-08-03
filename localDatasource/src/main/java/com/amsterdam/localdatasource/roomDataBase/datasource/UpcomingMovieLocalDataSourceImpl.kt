package com.amsterdam.localdatasource.roomDataBase.datasource

import androidx.room.Transaction
import com.amsterdam.localdatasource.roomDataBase.daos.MovieDao
import com.amsterdam.localdatasource.roomDataBase.daos.UpcomingMovieDao
import com.amsterdam.repository.datasource.local.UpcomingMovieLocalSource
import com.amsterdam.repository.dto.local.LocalMovieDto
import com.amsterdam.repository.dto.local.UpcomingMovieDto
import kotlinx.datetime.Instant
import javax.inject.Inject

class UpcomingMovieLocalDataSourceImpl @Inject constructor(
    private val movieDao: MovieDao,
    private val upcomingMovieDao: UpcomingMovieDao
) : UpcomingMovieLocalSource {

    @Transaction
    override suspend fun addUpcomingMovies(movies: List<LocalMovieDto>) {
        movieDao.insertMovies(movies)
        val entries = movies.map { movie ->
            UpcomingMovieDto(
                movieId = movie.movieId,
                storedLanguage = movie.storedLanguage,
                dateAdded = movie.insertedDate
            )
        }
        upcomingMovieDao.insertUpcomingMovies(entries)
    }

    override suspend fun deleteExpiredUpcomingMovies(
        expirationTime: Instant,
        storedLanguage: String
    ) {
        upcomingMovieDao.deleteExpiredUpcomingMovies(expirationTime, storedLanguage)
    }
}
