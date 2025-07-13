package com.amsterdam.repository.repository

import com.amsterdam.domain.repository.CategoryRepository
import com.amsterdam.entity.Category
import com.amsterdam.repository.datasource.local.LocalCategoryDataSource
import com.amsterdam.repository.datasource.remote.RemoteCategoryDatasource
import com.amsterdam.repository.mapper.local.CategoryLocalMapper

class CategoryRepositoryImpl(
    private val remoteCategoryDatasource: RemoteCategoryDatasource,
    private val localCategoryDatasource: LocalCategoryDataSource,
    private val categoryLocalMapper: CategoryLocalMapper
) : CategoryRepository {
    override suspend fun getMovieCategories(): List<Category> {
        val localCategories = localCategoryDatasource.getAllMovieCategories()
        val movieCategories = localCategories.ifEmpty {
            val remoteCategories = remoteCategoryDatasource.getMovieCategories()
            remoteCategories.also {
                localCategoryDatasource.upsertAllMovieCategories(
                    categoryLocalMapper.mapToLocalMovieCategories(it)
                )
            }
            categoryLocalMapper.mapToLocalMovieCategories(remoteCategories)
        }
        return categoryLocalMapper.mapListFromMovieLocal(movieCategories)
    }

    override suspend fun getTvShowCategories(): List<Category> {
        val localCategories = localCategoryDatasource.getAllTvShowCategories()
        val movieCategories = localCategories.ifEmpty {
            val remoteCategories = remoteCategoryDatasource.getTvShowCategories()
            remoteCategories.also {
                localCategoryDatasource.upsertAllTvShowCategories(
                    categoryLocalMapper.mapToLocalTvShowCategories(it)
                )
            }
            categoryLocalMapper.mapToLocalTvShowCategories(remoteCategories)
        }
        return categoryLocalMapper.mapListFromTvShowLocal(movieCategories)
    }
}