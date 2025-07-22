package com.example.localdatasource.roomDataBase.daos

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.example.repository.dto.local.LocalMovieDto
import com.example.repository.dto.local.SearchMovieCrossRefDto
import com.example.repository.dto.local.relation.MovieWithCategories
import com.example.repository.dto.local.utils.DatabaseConstants
import com.example.repository.dto.local.utils.SearchType

@Dao
interface MovieDao {
    @Transaction
    @Query(
        """
        SELECT * FROM movies 
        WHERE (movieId, storedLanguage) IN (
            SELECT movieId, storedLanguage FROM ${DatabaseConstants.SEARCH_MOVIE_CROSS_REF_TABLE} 
            WHERE searchKeyword = :keyword
            AND searchType = :searchType
            AND storedLanguage = :storedLanguage
            LIMIT :limit OFFSET :offset
        )
        """
    )
    suspend fun getMoviesByKeywordAndSearchType(
        keyword: String,
        searchType: SearchType,
        storedLanguage: String,
        limit: Int,
        offset: Int
    ): List<MovieWithCategories>

    @Upsert
    suspend fun insertMovies(movies: List<LocalMovieDto>)

    @Upsert
    suspend fun insertSearchEntries(entries: List<SearchMovieCrossRefDto>)

    @Query(" SELECT * FROM movies WHERE movieId = :movieId")
    fun getMovieById(movieId : Long) : LocalMovieDto
}
