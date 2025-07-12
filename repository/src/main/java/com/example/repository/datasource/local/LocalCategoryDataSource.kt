package com.example.repository.datasource.local

import com.example.repository.dto.local.LocalCategoryDto

interface LocalCategoryDataSource {
    suspend fun upsertCategory(category: LocalCategoryDto)
    suspend fun upsertAllCategories(categories: List<LocalCategoryDto>)
    suspend fun getAllCategories(): List<LocalCategoryDto>
}
