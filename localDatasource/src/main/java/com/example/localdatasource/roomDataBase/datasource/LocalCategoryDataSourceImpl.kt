package com.example.localdatasource.roomDataBase.datasource

import com.example.localdatasource.roomDataBase.daos.CategoryDao
import com.example.repository.datasource.local.LocalCategoryDataSource
import com.example.repository.dto.local.LocalMovieCategoryDto
import com.example.repository.dto.local.LocalTvShowCategoryDto

class LocalCategoryDataSourceImpl(
    private val dao: CategoryDao,
) : LocalCategoryDataSource {
    override suspend fun upsertAllMovieCategories(categories: List<LocalMovieCategoryDto>) {
        dao.upsertAllMovieCategories(categories)
    }

    override suspend fun upsertAllTvShowCategories(categories: List<LocalTvShowCategoryDto>) {
        dao.upsertAllTvShowCategories(categories)
    }

    override suspend fun getAllMovieCategories(): List<LocalMovieCategoryDto> {
        return dao.getAllMovieCategories()
    }

    override suspend fun getAllTvShowCategories(): List<LocalTvShowCategoryDto> {
        return dao.getAllTvShowCategories()
    }

    override suspend fun getMovieCategories(movieId: Long): List<LocalMovieCategoryDto> {
       return dao.getMovieCategories(movieId)
    }
}



