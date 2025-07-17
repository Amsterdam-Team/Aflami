package com.example.repository.repository

import com.example.domain.repository.CategoryRepository
import com.example.entity.Category
import com.example.repository.datasource.local.CategoryLocalSource
import com.example.repository.datasource.remote.CategoryRemoteSource
import com.example.repository.dto.remote.RemoteCategoryResponse
import com.example.repository.mapper.local.CategoryLocalMapper
import com.example.repository.mapper.remote.CategoryRemoteMapper
import com.example.repository.utils.tryToExecute

class CategoryRepositoryImpl(
    private val categoryRemoteSource: CategoryRemoteSource,
    private val categoryLocalSource: CategoryLocalSource,
    private val categoryLocalMapper: CategoryLocalMapper,
    private val categoryRemoteMapper: CategoryRemoteMapper
) : CategoryRepository {

    override suspend fun getMovieCategories(): List<Category> {
        val categoriesFromLocal = getMovieCategoriesFromLocal()
        if (categoriesFromLocal.isNotEmpty()) return categoriesFromLocal
        return tryToExecute(
            function = { categoryRemoteSource.getMovieCategories() },
            onSuccess = { movieCategories ->
                saveMovieCategoriesToDatabase(movieCategories)
                categoryRemoteMapper.mapToCategories(movieCategories)
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
                categoryRemoteMapper.mapToCategories(tvShowCategories)
            },
            onFailure = { aflamiException -> throw aflamiException },
        )
    }

    private suspend fun getMovieCategoriesFromLocal(): List<Category> {
        return tryToExecute(
            function = { categoryLocalSource.getMovieCategories() },
            onSuccess = { localCategories ->
                categoryLocalMapper.mapToMovieCategories(localCategories)
                    .map { category ->
                        Category(
                            id = category.ordinal.toLong(),
                            name = category.name,
                            imageUrl = ""
                        )
                    }
            },
            onFailure = { aflamiException -> throw aflamiException }
        )
    }

    private suspend fun saveMovieCategoriesToDatabase(movieCategories: RemoteCategoryResponse) {
        tryToExecute(
            function = {
                categoryLocalSource.upsertMovieCategories(
                    categoryRemoteMapper.mapToLocalMovieCategories(movieCategories)
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
                ).map { category ->
                    Category(
                        id = category.ordinal.toLong(),
                        name = category.name,
                        imageUrl = ""
                    )
                }
            },
            onFailure = { aflamiException -> throw aflamiException }
        )
    }

    private suspend fun saveTvShowCategoriesToDatabase(tvShowCategories: RemoteCategoryResponse) {
        tryToExecute(
            function = {
                categoryLocalSource.upsertTvShowCategories(
                    categoryRemoteMapper.mapToLocalTvShowCategories(tvShowCategories)
                )
            },
            onSuccess = {},
            onFailure = {}
        )
    }
}