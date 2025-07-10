package com.example.repository.datasource.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.repository.dto.local.LocalMovieDto
import com.example.repository.dto.local.SearchDto
import com.example.repository.dto.local.relation.MovieWithCategories
import com.example.repository.dto.local.utils.SearchType

@Dao
interface LocalMovieDataSource {
    @Query("""
        SELECT * FROM movies 
        WHERE id IN (
            SELECT movieId FROM search 
            WHERE name = :keyword and searchType = :searchType
        )
    """)
    suspend fun getMoviesByKeywordAndSearchType(keyword: String, searchType: SearchType): List<MovieWithCategories>

    @Insert
    suspend fun addAllMovies(movies: List<LocalMovieDto>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMoviesWithItsSearch(searches: List<SearchDto>)
}
