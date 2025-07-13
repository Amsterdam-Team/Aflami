package com.amsterdam.localdatasource.roomDataBase.daos

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.amsterdam.repository.dto.local.LocalMovieDto
import com.amsterdam.repository.dto.local.SearchMovieCrossRefDto
import com.amsterdam.repository.dto.local.relation.MovieWithCategories
import com.amsterdam.repository.dto.local.utils.SearchType

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
}
