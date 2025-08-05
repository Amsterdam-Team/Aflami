package com.amsterdam.repository.repository

import com.amsterdam.domain.repository.CategoryRepository
import com.amsterdam.entity.Category
import com.amsterdam.repository.datasource.local.AppPreferences
import com.amsterdam.repository.datasource.local.CategoryLocalSource
import com.amsterdam.repository.datasource.remote.CategoryRemoteSource
import com.amsterdam.repository.dto.local.LocalMovieCategoryDto
import com.amsterdam.repository.dto.remote.RemoteCategoryResponse
import com.amsterdam.repository.mapper.local.toEntityList
import com.amsterdam.repository.mapper.remote.toEntityList
import com.amsterdam.repository.mapper.remoteToLocal.toLocalDtoList
import com.amsterdam.repository.mapper.remoteToLocal.toLocalTvShowCategoryDtoList
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(
    private val categoryRemoteSource: CategoryRemoteSource,
    private val categoryLocalSource: CategoryLocalSource,
    private val preferences: AppPreferences
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
        return onSuccessGetMovieCategoriesFromLocal(
            categoryLocalSource.getMovieCategories(
                preferences.getAppLanguage().first()
            )
        )
    }

    private fun onSuccessGetMovieCategoriesFromLocal(
        localCategories: List<LocalMovieCategoryDto>
    ): List<Category> {
        return localCategories.toEntityList()
    }

    private suspend fun onSuccessLoadMovieCategories(
        movieCategories: RemoteCategoryResponse
    ): List<Category> {
        return saveMovieCategoriesToDatabase(movieCategories)
            .let { movieCategories.genres.toEntityList() }
    }

    private suspend fun saveMovieCategoriesToDatabase(
        movieCategories: RemoteCategoryResponse
    ) {
        categoryLocalSource.upsertMovieCategories(
            movieCategories.genres.toLocalDtoList(
                preferences.getAppLanguage().first()
            )
        )
    }

    private suspend fun getTvShowCategoriesFromLocal(): List<Category> {
          return categoryLocalSource.getTvShowCategories(
                preferences.getAppLanguage().first()
            ).toEntityList()
    }

    private suspend fun onSuccessLoadTvShowCategories(
        tvShowCategories: RemoteCategoryResponse
    ): List<Category> {
        return saveTvShowCategoriesToDatabase(tvShowCategories).let {
            tvShowCategories.genres.toEntityList()
        }
    }

    private suspend fun saveTvShowCategoriesToDatabase(
        tvShowCategories: RemoteCategoryResponse
    ) {
        categoryLocalSource.upsertTvShowCategories(
                tvShowCategories.genres.toLocalTvShowCategoryDtoList(preferences.getAppLanguage().first())
        )
    }
}