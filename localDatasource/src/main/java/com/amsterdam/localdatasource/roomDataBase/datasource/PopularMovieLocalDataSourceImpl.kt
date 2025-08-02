package com.amsterdam.localdatasource.roomDataBase.datasource

import androidx.room.Transaction
import com.amsterdam.localdatasource.roomDataBase.daos.MovieDao
import com.amsterdam.localdatasource.roomDataBase.daos.PopularMovieDao
import com.amsterdam.repository.datasource.local.PopularMovieLocalSource
import com.amsterdam.repository.dto.local.LocalMovieDto
import com.amsterdam.repository.dto.local.PopularMovieDto
import javax.inject.Inject


class PopularMovieLocalDataSourceImpl @Inject constructor(
    private val movieDao: MovieDao,
    private val popularMovieDao: PopularMovieDao
) : PopularMovieLocalSource {
    @Transaction
    override suspend fun addPopularMovies(movies: List<LocalMovieDto>) {
        movieDao.insertMovies(movies)
        val entries = movies.map { movie ->
            PopularMovieDto(
                movieId = movie.movieId,
                storedLanguage = movie.storedLanguage,
                dateAdded = movie.insertedDate
            )
        }
        popularMovieDao.insertPopularMovies(entries)
    }

    override suspend fun deleteExpiredPopularMovies(
        expirationTime: Long,
        storedLanguage: String
    ) {
        popularMovieDao.deleteExpiredPopularMovies(expirationTime, storedLanguage)
    }
}
