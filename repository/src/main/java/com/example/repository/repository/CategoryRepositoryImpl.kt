package com.example.repository.repository

import com.example.domain.repository.CategoryRepository
import com.example.entity.Category
import com.example.repository.datasource.local.CategoryLocalSource
import com.example.repository.datasource.remote.RemoteCategoryDatasource
import com.example.repository.mapper.local.CategoryLocalMapper

class CategoryRepositoryImpl(
    private val remoteCategoryDatasource: RemoteCategoryDatasource,
    private val categoryDatasource: CategoryLocalSource,
    private val categoryLocalMapper: CategoryLocalMapper
) : CategoryRepository {
    override suspend fun getMovieCategories(): List<Category> {
        val localCategories = categoryDatasource.getAllMovieCategories()
        val movieCategories = localCategories.ifEmpty {
            val remoteCategories = remoteCategoryDatasource.getMovieCategories()
            remoteCategories.also {
                categoryDatasource.upsertAllMovieCategories(
                    categoryLocalMapper.mapToLocalMovieCategories(it)
                )
            }
            categoryLocalMapper.mapToLocalMovieCategories(remoteCategories)
        }
        return categoryLocalMapper.mapListFromMovieLocal(movieCategories)
    }

    override suspend fun getTvShowCategories(): List<Category> {
        val localCategories = categoryDatasource.getAllTvShowCategories()
        val movieCategories = localCategories.ifEmpty {
            val remoteCategories = remoteCategoryDatasource.getTvShowCategories()
            remoteCategories.also {
                categoryDatasource.upsertAllTvShowCategories(
                    categoryLocalMapper.mapToLocalTvShowCategories(it)
                )
            }
            categoryLocalMapper.mapToLocalTvShowCategories(remoteCategories)
        }
        return categoryLocalMapper.mapListFromTvShowLocal(movieCategories)
    }
}