package com.example.localdatasource.roomDataBase.daos

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.example.repository.dto.local.LocalMovieDto
import com.example.repository.dto.local.SearchMovieCrossRefDto
import com.example.repository.dto.local.relation.MovieWithCategories
import com.example.repository.dto.local.utils.DatabaseContract
import com.example.repository.dto.local.utils.SearchType

@Dao
interface MovieDao {

    @Transaction
    @Query(
        """
        SELECT * FROM movies 
        WHERE movieId IN (
            SELECT movieId FROM ${DatabaseContract.RECENT_SEARCH_TABLE} 
            WHERE searchKeyword = :keyword 
              AND searchType = :searchType
        )
        """
    )
    suspend fun getMoviesByKeywordAndSearchType(
        keyword: String,
        searchType: SearchType
    ): List<MovieWithCategories>

    @Transaction
    @Query(
        """
        SELECT * FROM ${DatabaseContract.SEARCH_MOVIE_CROSS_REF_TABLE} 
        WHERE searchKeyword = :keyword 
        AND searchType = :searchType
        """
    )
    suspend fun getSearchMoviesCrossRef(
        keyword: String,
        searchType: SearchType
    ): List<SearchMovieCrossRefDto>

    @Upsert
    suspend fun insertMovies(movies: List<LocalMovieDto>)

    @Upsert
    suspend fun insertSearchEntries(entries: List<SearchMovieCrossRefDto>)
}
