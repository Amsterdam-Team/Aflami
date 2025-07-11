package com.example.localdatasource.roomDataBase.datasource

import com.example.localdatasource.roomDataBase.daos.CategoryDao
import com.example.repository.datasource.local.LocalCategoryDataSource
import com.example.repository.dto.local.LocalCategoryDto

class LocalCategoryDataSourceImpl(
    private val dao: CategoryDao
) : LocalCategoryDataSource {

    override suspend fun insertOrReplaceCategory(category: LocalCategoryDto) {
        dao.insertOrReplaceCategory(category)
    }

    override suspend fun insertOrReplaceAll(categories: List<LocalCategoryDto>) {
        dao.insertOrReplaceAll(categories)
    }

    override suspend fun getAllCategories(): List<LocalCategoryDto> {
        return dao.getAll()
    }
}



