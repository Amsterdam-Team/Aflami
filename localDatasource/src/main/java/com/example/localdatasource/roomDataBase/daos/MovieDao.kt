package com.example.localdatasource.roomDataBase.daos

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.example.repository.dto.local.LocalMovieDto
import com.example.repository.dto.local.LocalSearchDto
import com.example.repository.dto.local.relation.MovieWithCategories
import com.example.repository.dto.local.utils.SearchType

@Dao
interface MovieDao {

    @Transaction
    @Query(
        """
        SELECT * FROM movies 
        WHERE id IN (
            SELECT movieId FROM SearchDto 
            WHERE searchKeyword = :keyword 
              AND searchType = :searchType
              AND (:rating IS NULL OR rating = :rating)
        )
        AND (:category IS NULL OR id IN (
            SELECT movieId FROM movie_category_cross_ref
            WHERE categoryId IN (
                SELECT id FROM movie_categories WHERE name = :category
            )
        ))
        """
    )
    suspend fun getMoviesByKeywordAndSearchType(
        keyword: String,
        searchType: SearchType,
        rating: Int?,
        category: String?
    ): List<MovieWithCategories>

    @Upsert
    suspend fun insertMovies(movies: List<LocalMovieDto>)

    @Upsert
    suspend fun insertSearchEntries(entries: List<LocalSearchDto>)
}
