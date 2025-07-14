package com.example.localdatasource.roomDataBase.datasource

import com.example.localdatasource.roomDataBase.daos.CategoryDao
import com.example.repository.datasource.local.CategoryLocalSource
import com.example.repository.dto.local.LocalMovieCategoryDto
import com.example.repository.dto.local.LocalTvShowCategoryDto

class CategoryLocalSourceImpl(
    private val dao: CategoryDao
) : CategoryLocalSource {
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
}



