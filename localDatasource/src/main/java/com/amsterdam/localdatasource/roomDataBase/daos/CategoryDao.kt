package com.amsterdam.localdatasource.roomDataBase.daos

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.amsterdam.repository.dto.local.MovieCategoryLocalDto
import com.amsterdam.repository.dto.local.TvShowCategoryLocalDto
import com.amsterdam.repository.dto.local.utils.DatabaseConstants

@Dao
interface CategoryDao {
    @Upsert
    suspend fun upsertAllMovieCategories(categories: List<MovieCategoryLocalDto>)

    @Upsert
    suspend fun upsertAllTvShowCategories(categories: List<TvShowCategoryLocalDto>)

    @Query("SELECT * FROM ${DatabaseConstants.MOVIE_CATEGORY_TABLE}")
    suspend fun getAllMovieCategories(): List<MovieCategoryLocalDto>

    @Query("SELECT * FROM ${DatabaseConstants.TV_SHOW_CATEGORY_TABLE}")
    suspend fun getAllTvShowCategories(): List<TvShowCategoryLocalDto>
}