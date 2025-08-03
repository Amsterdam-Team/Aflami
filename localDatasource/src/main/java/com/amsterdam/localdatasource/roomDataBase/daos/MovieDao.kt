package com.amsterdam.localdatasource.roomDataBase.daos

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.amsterdam.repository.dto.local.LocalMovieDto
import com.amsterdam.repository.dto.local.MovieCategoryCrossRefDto
import com.amsterdam.repository.dto.local.PopularMovieDto
import com.amsterdam.repository.dto.local.TopRatedMovieDto
import com.amsterdam.repository.dto.local.UpcomingMovieDto
import com.amsterdam.repository.dto.local.relation.MovieWithCategories
import com.amsterdam.repository.dto.local.utils.DatabaseConstants
import kotlinx.datetime.Instant

@Dao
interface MovieDao {
    @Upsert
    suspend fun insertMovies(movies: List<LocalMovieDto>)

    @Upsert
    suspend fun insertMovie(movies: LocalMovieDto)

    @Query(" SELECT * FROM ${DatabaseConstants.MOVIE_TABLE} WHERE movieId = :movieId and storedLanguage = :storedLanguage")
    suspend fun getMovieById(movieId: Long, storedLanguage: String): LocalMovieDto?

    @Upsert
    suspend fun insertMovieCategoryCrossRefs(crossRefs: List<MovieCategoryCrossRefDto>)

    @Query(
        """
        SELECT DISTINCT movie.* FROM ${DatabaseConstants.MOVIE_TABLE} AS movie
        INNER JOIN ${DatabaseConstants.POPULAR_MOVIE_TABLE} AS popularMovies
        ON movie.movieId = popularMovies.movieId
        LEFT JOIN ${DatabaseConstants.MOVIE_CATEGORY_CROSS_REF_TABLE} AS categoryCrossRef
        ON movie.movieId = categoryCrossRef.movieId
        WHERE movie.storedLanguage = popularMovies.storedLanguage
        AND movie.storedLanguage = :storedLanguage
    """
    )
    suspend fun getPopularMovies(storedLanguage: String): List<MovieWithCategories>

    @Query(
        """
        SELECT * FROM ${DatabaseConstants.MOVIE_TABLE} AS movie
        INNER JOIN ${DatabaseConstants.TOP_RATED_MOVIE_TABLE} As topRatedMovie
        ON movie.movieId = topRatedMovie.movieId 
        WHERE movie.storedLanguage = topRatedMovie.storedLanguage
        AND movie.storedLanguage = :storedLanguage
    """
    )
    suspend fun getTopRatedMovies(storedLanguage: String): List<LocalMovieDto>


    @Query(
        """
        SELECT DISTINCT movie.* FROM ${DatabaseConstants.MOVIE_TABLE} AS movie
        INNER JOIN ${DatabaseConstants.UPCOMING_MOVIE_TABLE} AS upcomingMovies
        ON movie.movieId = upcomingMovies.movieId
        LEFT JOIN ${DatabaseConstants.MOVIE_CATEGORY_CROSS_REF_TABLE} AS categoryCrossRef
        ON movie.movieId = categoryCrossRef.movieId
        WHERE movie.storedLanguage = upcomingMovies.storedLanguage
        AND movie.storedLanguage = :storedLanguage
    """
    )
    suspend fun getUpcomingMovies(storedLanguage: String): List<MovieWithCategories>

    @Upsert
    suspend fun insertPopularMovies(movies: List<PopularMovieDto>)

    @Query(
        """
            DELETE FROM ${DatabaseConstants.POPULAR_MOVIE_TABLE}
            WHERE dateAdded < :expirationTime and storedLanguage = :storedLanguage
        """
    )
    suspend fun deleteExpiredPopularMovies(expirationTime: Instant, storedLanguage: String)

    @Upsert
    suspend fun insertTopRatedMovies(movies: List<TopRatedMovieDto>)

    @Query(
        """
            DELETE FROM ${DatabaseConstants.TOP_RATED_MOVIE_TABLE} 
            WHERE dateAdded < :expirationTime AND storedLanguage = :storedLanguage
    """
    )
    suspend fun deleteAllExpiredTopRatedMovies(expirationTime: Instant, storedLanguage: String)

    @Upsert
    suspend fun insertUpcomingMovies(movies: List<UpcomingMovieDto>)

    @Query(
        """
            DELETE FROM ${DatabaseConstants.UPCOMING_MOVIE_TABLE}
            WHERE dateAdded < :expirationTime and storedLanguage = :storedLanguage
        """
    )
    suspend fun deleteExpiredUpcomingMovies(expirationTime: Instant, storedLanguage: String)
}
