package com.amsterdam.localdatasource.roomDataBase.daos

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.amsterdam.repository.dto.local.LocalMovieCategoryDto
import com.amsterdam.repository.dto.local.LocalTvShowCategoryDto

@Dao
interface CategoryDao {
    @Upsert
    suspend fun upsertAllMovieCategories(categories: List<LocalMovieCategoryDto>)

    @Upsert
    suspend fun upsertAllTvShowCategories(categories: List<LocalTvShowCategoryDto>)

    @Query("SELECT * FROM movie_categories")
    suspend fun getAllMovieCategories(): List<LocalMovieCategoryDto>

    @Query("SELECT * FROM tv_show_categories")
    suspend fun getAllTvShowCategories(): List<LocalTvShowCategoryDto>
}