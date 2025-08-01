package com.amsterdam.localdatasource.roomDataBase.daos

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.amsterdam.repository.dto.local.LocalMovieCategoryDto
import com.amsterdam.repository.dto.local.LocalTvShowCategoryDto
import com.amsterdam.repository.dto.local.utils.DatabaseConstants

@Dao
interface CategoryDao {
    @Upsert
    suspend fun upsertAllMovieCategories(categories: List<LocalMovieCategoryDto>)

    @Upsert
    suspend fun upsertAllTvShowCategories(categories: List<LocalTvShowCategoryDto>)

    @Query("SELECT * FROM ${DatabaseConstants.MOVIE_CATEGORY_TABLE} WHERE storedLanguage = :storedLanguage")
    suspend fun getAllMovieCategories(storedLanguage: String): List<LocalMovieCategoryDto>

    @Query("SELECT * FROM ${DatabaseConstants.TV_SHOW_CATEGORY_TABLE} WHERE storedLanguage = :storedLanguage")
    suspend fun getAllTvShowCategories(storedLanguage: String): List<LocalTvShowCategoryDto>
}