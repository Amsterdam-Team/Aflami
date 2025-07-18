package com.example.repository.repository

import com.example.domain.repository.CategoryRepository
import com.example.entity.Category
import com.example.repository.datasource.local.CategoryLocalSource
import com.example.repository.datasource.remote.CategoryRemoteSource
import com.example.repository.dto.local.LocalMovieCategoryDto
import com.example.repository.dto.remote.RemoteCategoryResponse
import com.example.repository.mapper.local.CategoryLocalMapper
import com.example.repository.mapper.remote.CategoryRemoteMapper

class CategoryRepositoryImpl(
    private val categoryRemoteSource: CategoryRemoteSource,
    private val categoryLocalSource: CategoryLocalSource,
    private val categoryLocalMapper: CategoryLocalMapper,
    private val categoryRemoteMapper: CategoryRemoteMapper
) : CategoryRepository {
    override suspend fun getMovieCategories(): List<Category> {
        return getMovieCategoriesFromLocal()
            .takeIf { localMovieCategories -> localMovieCategories.isNotEmpty() }
            ?: onSuccessLoadMovieCategories(categoryRemoteSource.getMovieCategories())
    }

    override suspend fun getTvShowCategories(): List<Category> {
        return getTvShowCategoriesFromLocal()
            .takeIf { localTvShowCategories -> localTvShowCategories.isNotEmpty() }
            ?: onSuccessLoadTvShowCategories(categoryRemoteSource.getTvShowCategories())
    }

    private suspend fun getMovieCategoriesFromLocal(): List<Category> {
        return onSuccessGetMovieCategoriesFromLocal(categoryLocalSource.getMovieCategories())
    }

    private fun onSuccessGetMovieCategoriesFromLocal(
        localCategories: List<LocalMovieCategoryDto>
    ): List<Category> {
        return categoryLocalMapper
            .mapToMovieCategories(localCategories)
            .map { category ->
                Category(id = category.ordinal.toLong(), name = category.name, imageUrl = "")
            }
    }

    private suspend fun onSuccessLoadMovieCategories(
        movieCategories: RemoteCategoryResponse
    ): List<Category> {
        return saveMovieCategoriesToDatabase(movieCategories)
            .let { categoryRemoteMapper.mapToCategories(movieCategories) }
    }

    private suspend fun saveMovieCategoriesToDatabase(
        movieCategories: RemoteCategoryResponse
    ) {
        categoryLocalSource.upsertMovieCategories(
            categoryRemoteMapper.mapToLocalMovieCategories(movieCategories)
        )
    }

    private suspend fun getTvShowCategoriesFromLocal(): List<Category> {
        return categoryLocalMapper.mapToCategories(categoryLocalSource.getTvShowCategories())
    }

    private suspend fun onSuccessLoadTvShowCategories(
        tvShowCategories: RemoteCategoryResponse
    ): List<Category> {
        return saveTvShowCategoriesToDatabase(tvShowCategories).let {
            categoryRemoteMapper.mapToCategories(tvShowCategories)
        }
    }

    private suspend fun saveTvShowCategoriesToDatabase(
        tvShowCategories: RemoteCategoryResponse
    ) {
        categoryLocalSource.upsertTvShowCategories(
            categoryRemoteMapper.mapToLocalTvShowCategories(tvShowCategories)
        )
    }
}