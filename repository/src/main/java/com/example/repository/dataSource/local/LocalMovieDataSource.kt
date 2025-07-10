package com.example.repository.dataSource.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.example.repository.dto.local.LocalMovieDto
import com.example.repository.dto.local.relation.MovieWithCategories
import com.example.repository.dto.local.relation.SearchType

@Dao
interface LocalMovieDataSource {
    @Query("""
        SELECT * FROM movies 
        WHERE id IN (
            SELECT movieId FROM search 
            WHERE name = :keyword and searchType = :searchType
        )
    """)
    suspend fun getMoviesByKeyword(keyword: String, searchType: SearchType): List<MovieWithCategories>

    @Insert
    suspend fun addAllMovies(movies: List<LocalMovieDto>)
}
