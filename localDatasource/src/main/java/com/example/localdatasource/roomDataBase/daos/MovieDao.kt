package com.example.localdatasource.roomDataBase.daos

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.example.repository.dto.local.LocalMovieDto
import com.example.repository.dto.local.SearchMovieCrossRefDto
import com.example.repository.dto.local.relation.MovieWithCategories
import com.example.repository.dto.local.utils.SearchType

@Dao
interface MovieDao {

    @Transaction
    @Query(
        """
        SELECT * FROM movies 
        WHERE movieId IN (
            SELECT movieId FROM SearchDto 
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
        SELECT * FROM search_movie_cross_ref 
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

    @Query(
        """
    SELECT m.* FROM movies AS m
    INNER JOIN search_movie_cross_ref AS x
    ON m.movieId = x.movieId
    WHERE x.searchKeyword = :keyword 
      AND x.searchType = :searchType
    ORDER BY m.movieId ASC
"""
    )
    fun getMoviesPagingSourceByKeywordAndType(
        keyword: String,
        searchType: SearchType
    ): PagingSource<Int, LocalMovieDto>


}
