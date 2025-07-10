package com.example.localdatasource.roomDatabase

import com.example.localdatasource.roomDatabase.daos.LocalCategoryDao
import com.example.repository.datasource.local.LocalCategoryDataSource
import com.example.repository.dto.local.LocalCategoryDto

class LocalCategoryDataSourceImpl(
    private val dao: LocalCategoryDao
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



