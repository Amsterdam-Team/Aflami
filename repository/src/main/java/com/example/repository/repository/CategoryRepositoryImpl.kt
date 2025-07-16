package com.example.repository.repository

import com.example.domain.repository.CategoryRepository
import com.example.entity.Category
import com.example.repository.datasource.local.CategoryLocalSource
import com.example.repository.datasource.remote.CategoryRemoteSource
import com.example.repository.dto.local.LocalMovieCategoryDto
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

    override suspend fun getMovieCategories(): List<Category> =
        getMovieCategoriesFromLocal()
            .takeIf { localMovieCategories -> localMovieCategories.isNotEmpty() }
            ?: tryToExecute(
            function = { categoryRemoteSource.getMovieCategories() },
                onSuccess = { movieCategories -> onSuccessLoadMovieCategories(movieCategories) },
            onFailure = { aflamiException -> throw aflamiException },
        )


    override suspend fun getTvShowCategories(): List<Category> =
        getTvShowCategoriesFromLocal()
            .takeIf { localTvShowCategories -> localTvShowCategories.isNotEmpty() }
            ?: tryToExecute(
            function = { categoryRemoteSource.getTvShowCategories() },
                onSuccess = { tvShowCategories -> onSuccessLoadTvShowCategories(tvShowCategories) },
            onFailure = { aflamiException -> throw aflamiException },
        )

    private suspend fun getMovieCategoriesFromLocal(): List<Category> =
        tryToExecute(
            function = { categoryLocalSource.getMovieCategories() },
            onSuccess = { localCategories -> onSuccessGetMovieCategoriesFromLocal(localCategories) },
            onFailure = { aflamiException -> throw aflamiException }
        )


    private fun onSuccessGetMovieCategoriesFromLocal(localCategories: List<LocalMovieCategoryDto>) =
        categoryLocalMapper.mapToMovieCategories(localCategories)
            .map { category ->
                Category(
                    id = category.ordinal.toLong(),
                    name = category.name,
                    image = ""
                )
            }

    private suspend fun CategoryRepositoryImpl.onSuccessLoadMovieCategories(
        movieCategories: RemoteCategoryResponse
    ): List<Category> =
        saveMovieCategoriesToDatabase(movieCategories)
            .let { categoryRemoteMapper.mapToCategories(movieCategories) }

    private suspend fun saveMovieCategoriesToDatabase(movieCategories: RemoteCategoryResponse) =
        tryToExecute(
            function = {
                categoryLocalSource.upsertMovieCategories(
                    categoryRemoteMapper.mapToLocalMovieCategories(movieCategories)
                )
            },
            onSuccess = {},
            onFailure = {}
        )

    private suspend fun getTvShowCategoriesFromLocal(): List<Category> =
        tryToExecute(
            function = { categoryLocalSource.getTvShowCategories() },
            onSuccess = { localCategories -> categoryLocalMapper.mapToCategories(localCategories) },
            onFailure = { aflamiException -> throw aflamiException }
        )

    private suspend fun CategoryRepositoryImpl.onSuccessLoadTvShowCategories(
        tvShowCategories: RemoteCategoryResponse
    ): List<Category> =
        saveTvShowCategoriesToDatabase(tvShowCategories).let {
            categoryRemoteMapper.mapToCategories(tvShowCategories)
        }

    private suspend fun saveTvShowCategoriesToDatabase(tvShowCategories: RemoteCategoryResponse) =
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