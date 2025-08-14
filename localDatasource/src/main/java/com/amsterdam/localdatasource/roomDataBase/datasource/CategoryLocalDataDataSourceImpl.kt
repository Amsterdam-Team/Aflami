package com.amsterdam.localdatasource.roomDataBase.datasource

import com.amsterdam.localdatasource.roomDataBase.daos.CategoryDao
import com.amsterdam.repository.datasource.local.CategoryLocalDataSource
import com.amsterdam.repository.dto.local.MovieCategoryLocalDto
import com.amsterdam.repository.dto.local.TvShowCategoryLocalDto
import javax.inject.Inject

class CategoryLocalDataDataSourceImpl @Inject constructor(
    private val dao: CategoryDao
) : CategoryLocalDataSource {
    override suspend fun upsertMovieCategories(categories: List<MovieCategoryLocalDto>) {
        dao.upsertAllMovieCategories(categories)
    }

    override suspend fun upsertTvShowCategories(categories: List<TvShowCategoryLocalDto>) {
        dao.upsertAllTvShowCategories(categories)
    }

    override suspend fun getMovieCategories(): List<MovieCategoryLocalDto> {
        return dao.getAllMovieCategories()
    }

    override suspend fun getTvShowCategories(): List<TvShowCategoryLocalDto> {
        return dao.getAllTvShowCategories()
    }

}


