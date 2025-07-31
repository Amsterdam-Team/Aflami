package com.amsterdam.repository.repository

import com.amsterdam.domain.repository.CategoryRepository
import com.amsterdam.entity.Category
import com.amsterdam.repository.datasource.local.AppPreferences
import com.amsterdam.repository.datasource.local.CategoryLocalSource
import com.amsterdam.repository.datasource.remote.CategoryRemoteSource
import com.amsterdam.repository.dto.local.LocalMovieCategoryDto
import com.amsterdam.repository.dto.remote.RemoteCategoryResponse
import com.amsterdam.repository.mapper.local.MovieCategoryLocalMapper
import com.amsterdam.repository.mapper.local.TvShowCategoryLocalMapper
import com.amsterdam.repository.mapper.remote.CategoryRemoteMapper
import com.amsterdam.repository.mapper.remoteToLocal.MovieCategoryRemoteLocalMapper
import com.amsterdam.repository.mapper.remoteToLocal.TvShowCategoryRemoteLocalMapper
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(
    private val categoryRemoteSource: CategoryRemoteSource,
    private val categoryLocalSource: CategoryLocalSource,
    private val preferences: AppPreferences,
    private val movieCategoryLocalMapper: MovieCategoryLocalMapper,
    private val categoryRemoteMapper: CategoryRemoteMapper,
    private val movieCategoryRemoteLocalMapper: MovieCategoryRemoteLocalMapper,
    private val tvShowCategoryRemoteLocalMapper: TvShowCategoryRemoteLocalMapper,
    private val tvShowCategoryLocalMapper: TvShowCategoryLocalMapper
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
                preferences.getDeviceLanguage().first()
            )
        )
    }

    private fun onSuccessGetMovieCategoriesFromLocal(
        localCategories: List<LocalMovieCategoryDto>
    ): List<Category> {
        return movieCategoryLocalMapper.toEntityList(localCategories)
    }

    private suspend fun onSuccessLoadMovieCategories(
        movieCategories: RemoteCategoryResponse
    ): List<Category> {
        return saveMovieCategoriesToDatabase(movieCategories)
            .let { categoryRemoteMapper.toEntityList(movieCategories.genres) }
    }

    private suspend fun saveMovieCategoriesToDatabase(
        movieCategories: RemoteCategoryResponse
    ) {
        categoryLocalSource.upsertMovieCategories(
            movieCategoryRemoteLocalMapper.toLocalList(
                movieCategories.genres,
                listOf(preferences.getDeviceLanguage())
            )
        )
    }

    private suspend fun getTvShowCategoriesFromLocal(): List<Category> {
        return tvShowCategoryLocalMapper.toEntityList(
            categoryLocalSource.getTvShowCategories(
                preferences.getDeviceLanguage().first()
            )
        )
    }

    private suspend fun onSuccessLoadTvShowCategories(
        tvShowCategories: RemoteCategoryResponse
    ): List<Category> {
        return saveTvShowCategoriesToDatabase(tvShowCategories).let {
            categoryRemoteMapper.toEntityList(tvShowCategories.genres)
        }
    }

    private suspend fun saveTvShowCategoriesToDatabase(
        tvShowCategories: RemoteCategoryResponse
    ) {
        categoryLocalSource.upsertTvShowCategories(
            tvShowCategoryRemoteLocalMapper.toLocalList(
                tvShowCategories.genres,
                listOf(preferences.getDeviceLanguage())
            )
        )
    }
}