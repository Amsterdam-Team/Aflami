package com.example.localdatasource.roomDataBase.datasource

import com.example.localdatasource.roomDataBase.daos.CategoryDao
import com.example.repository.datasource.local.LocalCategoryDataSource
import com.example.repository.dto.local.LocalCategoryDto

class LocalCategoryDataSourceImpl(
    private val dao: CategoryDao
) : LocalCategoryDataSource {

    override suspend fun upsertCategory(category: LocalCategoryDto) {
        dao.upsertCategory(category)
    }

    override suspend fun upsertAllCategories(categories: List<LocalCategoryDto>) {
        dao.upsertAllCategories(categories)
    }

    override suspend fun getAllCategories(): List<LocalCategoryDto> {
        return dao.getAllCategories()
    }
}



