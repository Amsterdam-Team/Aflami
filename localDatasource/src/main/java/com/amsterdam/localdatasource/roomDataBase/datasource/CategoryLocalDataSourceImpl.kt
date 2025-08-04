package com.amsterdam.localdatasource.roomDataBase.datasource

import com.amsterdam.localdatasource.roomDataBase.daos.CategoryDao
import com.amsterdam.repository.datasource.local.CategoryLocalSource
import com.amsterdam.repository.dto.local.LocalMovieCategoryDto
import com.amsterdam.repository.dto.local.LocalTvShowCategoryDto
import javax.inject.Inject

class CategoryLocalDataSourceImpl @Inject constructor(
    private val dao: CategoryDao
) : CategoryLocalSource {
    override suspend fun upsertMovieCategories(categories: List<LocalMovieCategoryDto>) {
        dao.upsertAllMovieCategories(categories)
    }

    override suspend fun upsertTvShowCategories(categories: List<LocalTvShowCategoryDto>) {
        dao.upsertAllTvShowCategories(categories)
    }

    override suspend fun getMovieCategories(storedLanguage: String): List<LocalMovieCategoryDto> {
        return dao.getAllMovieCategories(storedLanguage)
    }

    override suspend fun getTvShowCategories(storedLanguage: String): List<LocalTvShowCategoryDto> {
        return dao.getAllTvShowCategories(storedLanguage)
    }

}


