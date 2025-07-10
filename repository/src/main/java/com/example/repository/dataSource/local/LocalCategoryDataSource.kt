package com.example.repository.datasource.local

import com.example.repository.dto.local.LocalCategoryDto

interface LocalCategoryDataSource {
    suspend fun insertOrReplaceCategory(category: LocalCategoryDto)
    suspend fun insertOrReplaceAll(categories: List<LocalCategoryDto>)
    suspend fun getAllCategories(): List<LocalCategoryDto>
}
