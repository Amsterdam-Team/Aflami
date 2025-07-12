package com.example.repository.repository

import com.example.domain.repository.CategoryRepository
import com.example.entity.Category
import com.example.repository.datasource.local.LocalCategoryDataSource
import com.example.repository.datasource.remote.RemoteCategoryDatasource
import com.example.repository.mapper.local.CategoryLocalMapper

class CategoryRepositoryImpl(
    private val remoteCategoryDatasource: RemoteCategoryDatasource,
    private val localCategoryDatasource: LocalCategoryDataSource,
    private val categoryLocalMapper: CategoryLocalMapper
) : CategoryRepository {
    override suspend fun getMovieCategories(): List<Category> {
        val localCategories = localCategoryDatasource.getAllMovieCategories()
        val movieCategories = if (localCategories.isEmpty()) {
            val remoteCategories = remoteCategoryDatasource.getMovieCategories()
            remoteCategories.also {
                localCategoryDatasource.insertOrReplaceAllMovieCategories(
                    categoryLocalMapper.mapToLocalMovieCategories(it)
                )
            }
            categoryLocalMapper.mapToLocalMovieCategories(remoteCategories)
        } else {
            localCategories
        }
        return categoryLocalMapper.mapListFromMovieLocal(movieCategories)
    }

    override suspend fun getTvShowCategories(): List<Category> {
        val localCategories = localCategoryDatasource.getAllTvShowCategories()
        val movieCategories = if (localCategories.isEmpty()) {
            val remoteCategories = remoteCategoryDatasource.getTvShowCategories()
            remoteCategories.also {
                localCategoryDatasource.insertOrReplaceAllTvShowCategories(
                    categoryLocalMapper.mapToLocalTvShowCategories(it)
                )
            }
            categoryLocalMapper.mapToLocalTvShowCategories(remoteCategories)
        } else {
            localCategories
        }
        return categoryLocalMapper.mapListFromTvShowLocal(movieCategories)
    }
}