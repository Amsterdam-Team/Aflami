package com.example.repository.repository

import com.example.domain.repository.CategoryRepository
import com.example.entity.Category
import com.example.repository.datasource.local.CategoryLocalSource
import com.example.repository.datasource.remote.CategoryRemoteSource
import com.example.repository.dto.local.LocalMovieCategoryDto
import com.example.repository.dto.remote.RemoteCategoryResponse
import com.example.repository.mapper.local.MovieCategoryLocalMapper
import com.example.repository.mapper.local.TvShowCategoryLocalMapper
import com.example.repository.mapper.remote.CategoryRemoteMapper
import com.example.repository.mapper.remoteToLocal.MovieCategoryRemoteLocalMapper
import com.example.repository.mapper.remoteToLocal.TvShowCategoryRemoteLocalMapper

class CategoryRepositoryImpl(
    private val categoryRemoteSource: CategoryRemoteSource,
    private val categoryLocalSource: CategoryLocalSource,
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
        return onSuccessGetMovieCategoriesFromLocal(categoryLocalSource.getMovieCategories())
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
            movieCategoryRemoteLocalMapper.toLocalList(movieCategories.genres)
        )
    }

    private suspend fun getTvShowCategoriesFromLocal(): List<Category> {
        return tvShowCategoryLocalMapper.toEntityList(categoryLocalSource.getTvShowCategories())
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
            tvShowCategoryRemoteLocalMapper.toLocalList(tvShowCategories.genres)
        )
    }
}