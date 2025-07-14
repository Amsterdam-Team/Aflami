package com.example.repository.repository

import com.example.domain.repository.CategoryRepository
import com.example.entity.Category
import com.example.repository.datasource.local.CategoryLocalSource
import com.example.repository.datasource.remote.CategoryRemoteSource
import com.example.repository.mapper.local.CategoryLocalMapper

class CategoryRepositoryImpl(
    private val categoryRemoteSource: CategoryRemoteSource,
    private val categoryDatasource: CategoryLocalSource,
    private val categoryLocalMapper: CategoryLocalMapper
) : CategoryRepository {
    override suspend fun getMovieCategories(): List<Category> {
        val localCategories = categoryDatasource.getMovieCategories()
        val movieCategories = localCategories.ifEmpty {
            val remoteCategories = categoryRemoteSource.getMovieCategories()
            remoteCategories.also {
                categoryDatasource.upsertMovieCategories(
                    categoryLocalMapper.mapToLocalMovieCategories(it)
                )
            }
            categoryLocalMapper.mapToLocalMovieCategories(remoteCategories)
        }
        return categoryLocalMapper.mapToMovieCategories(movieCategories)
    }

    override suspend fun getTvShowCategories(): List<Category> {
        val localCategories = categoryDatasource.getTvShowCategories()
        val movieCategories = localCategories.ifEmpty {
            val remoteCategories = categoryRemoteSource.getTvShowCategories()
            remoteCategories.also {
                categoryDatasource.upsertTvShowCategories(
                    categoryLocalMapper.mapToLocalTvShowCategories(it)
                )
            }
            categoryLocalMapper.mapToLocalTvShowCategories(remoteCategories)
        }
        return categoryLocalMapper.mapToTvShowCategories(movieCategories)
    }
}