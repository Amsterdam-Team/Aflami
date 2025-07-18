package com.example.repository.repository

import com.example.domain.repository.CategoryRepository
import com.example.entity.Category
import com.example.repository.datasource.local.CategoryLocalSource
import com.example.repository.datasource.remote.CategoryRemoteSource
import com.example.repository.dto.remote.RemoteCategoryResponse
import com.example.repository.mapper.local.MovieCategoryLocalMapper
import com.example.repository.mapper.local.TvShowCategoryLocalMapper
import com.example.repository.mapper.remote.CategoryRemoteMapper
import com.example.repository.mapper.remoteToLocal.MovieCategoryRemoteLocalMapper
import com.example.repository.mapper.remoteToLocal.TvShowCategoryRemoteLocalMapper
import com.example.repository.utils.tryToExecute

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
        val categoriesFromLocal = getMovieCategoriesFromLocal()
        if (categoriesFromLocal.isNotEmpty()) return categoriesFromLocal
        return tryToExecute(
            function = { categoryRemoteSource.getMovieCategories() },
            onSuccess = { movieCategories ->
                saveMovieCategoriesToDatabase(movieCategories)
                categoryRemoteMapper.toEntityList(movieCategories.genres)
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
                categoryRemoteMapper.toEntityList(tvShowCategories.genres)
            },
            onFailure = { aflamiException -> throw aflamiException },
        )
    }

    private suspend fun getMovieCategoriesFromLocal(): List<Category> {
        return tryToExecute(
            function = { categoryLocalSource.getMovieCategories() },
            onSuccess = { localCategories ->
                movieCategoryLocalMapper.toEntityList(localCategories)
            },
            onFailure = { aflamiException -> throw aflamiException }
        )
    }

    private suspend fun saveMovieCategoriesToDatabase(movieCategories: RemoteCategoryResponse) {
        tryToExecute(
            function = {
                categoryLocalSource.upsertMovieCategories(
                    movieCategoryRemoteLocalMapper.toLocalList(movieCategories.genres)
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
                tvShowCategoryLocalMapper.toEntityList(localCategories)
            },
            onFailure = { aflamiException -> throw aflamiException }
        )
    }

    private suspend fun saveTvShowCategoriesToDatabase(tvShowCategories: RemoteCategoryResponse) {
        tryToExecute(
            function = {
                categoryLocalSource.upsertTvShowCategories(
                    tvShowCategoryRemoteLocalMapper.toLocalList(tvShowCategories.genres)
                )
            },
            onSuccess = {},
            onFailure = {}
        )
    }

}