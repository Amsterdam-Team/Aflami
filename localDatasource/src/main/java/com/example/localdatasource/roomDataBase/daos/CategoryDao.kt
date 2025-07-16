package com.example.localdatasource.roomDataBase.daos

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.repository.dto.local.LocalMovieCategoryDto
import com.example.repository.dto.local.LocalTvShowCategoryDto
import com.example.repository.dto.local.utils.DatabaseContract

@Dao
interface CategoryDao {
    @Upsert
    suspend fun upsertAllMovieCategories(categories: List<LocalMovieCategoryDto>)

    @Upsert
    suspend fun upsertAllTvShowCategories(categories: List<LocalTvShowCategoryDto>)

    @Query("SELECT * FROM ${DatabaseContract.MOVIE_CATEGORY_TABLE}")
    suspend fun getAllMovieCategories(): List<LocalMovieCategoryDto>

    @Query("SELECT * FROM ${DatabaseContract.TV_SHOW_CATEGORY_TABLE}")
    suspend fun getAllTvShowCategories(): List<LocalTvShowCategoryDto>
    @Query("SELECT * FROM movie_categories WHERE categoryId = :movieId")
    suspend fun getMovieCategories(movieId: Long) : List<LocalMovieCategoryDto>
}