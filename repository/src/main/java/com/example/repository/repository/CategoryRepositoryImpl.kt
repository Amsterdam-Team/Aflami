package com.example.repository.repository

import com.example.domain.repository.CategoryRepository
import com.example.entity.Category
import com.example.repository.datasource.local.CategoryLocalSource
import com.example.repository.datasource.remote.CategoryRemoteSource
import com.example.repository.dto.remote.RemoteCategoryResponse
import com.example.repository.mapper.local.CategoryLocalMapper
import com.example.repository.utils.tryToExecute

class CategoryRepositoryImpl(
    private val categoryRemoteSource: CategoryRemoteSource,
    private val categoryLocalSource: CategoryLocalSource,
    private val categoryLocalMapper: CategoryLocalMapper
) : CategoryRepository {

    override suspend fun getMovieCategories(): List<Category> {
        val categoriesFromLocal = getMovieCategoriesFromLocal()
        if (categoriesFromLocal.isNotEmpty()) return categoriesFromLocal
        return tryToExecute(
            function = { categoryRemoteSource.getMovieCategories() },
            onSuccess = { movieCategories ->
                saveMovieCategoriesToDatabase(movieCategories)
                categoryLocalMapper.mapToMovieCategories(movieCategories)
            },
            onFailure = { aflamiException -> throw aflamiException },
        )
    }

    override suspend fun getTvShowCategories(): List<Category> {
        val categoriesFromLocal = getTvShowCategoriesFromLocal()
        if (categoriesFromLocal.isNotEmpty()) return categoriesFromLocal
        return tryToExecute(
            function = { categoryRemoteSource.getTvShowCategories() },
            onSuccess = { tvShowCategories ->
                saveTvShowCategoriesToDatabase(tvShowCategories)
                categoryLocalMapper.mapToTvShowCategories(tvShowCategories)
            },
            onFailure = { aflamiException -> throw aflamiException },
        )
    }

    private suspend fun getMovieCategoriesFromLocal(): List<Category> {
        return tryToExecute(
            function = { categoryLocalSource.getMovieCategories() },
            onSuccess = { localCategories ->
                categoryLocalMapper.mapToMovieCategories(
                    localCategories
                )
            },
            onFailure = { aflamiException -> throw aflamiException }
        )
    }

    private suspend fun saveMovieCategoriesToDatabase(movieCategories: RemoteCategoryResponse) {
        tryToExecute(
            function = {
                categoryLocalSource.upsertMovieCategories(
                    categoryLocalMapper.mapToLocalMovieCategories(movieCategories)
                )
            },
            onSuccess = {},
            onFailure = {}
        )
    }

    private suspend fun getTvShowCategoriesFromLocal(): List<Category> {
        return tryToExecute(
            function = { categoryLocalSource.getTvShowCategories() },
            onSuccess = { localCategories ->
                categoryLocalMapper.mapToTvShowCategories(
                    localCategories
                )
            },
            onFailure = { aflamiException -> throw aflamiException }
        )
    }

    private suspend fun saveTvShowCategoriesToDatabase(tvShowCategories: RemoteCategoryResponse) {
        tryToExecute(
            function = {
                categoryLocalSource.upsertTvShowCategories(
                    categoryLocalMapper.mapToLocalTvShowCategories(tvShowCategories)
                )
            },
            onSuccess = {},
            onFailure = {}
        )
    }
}