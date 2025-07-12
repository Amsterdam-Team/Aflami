package com.example.domain.usecase

import com.example.domain.repository.CategoryRepository
import com.example.entity.Category

class GetTvShowCategoriesUseCase(
    private val categoryRepository: CategoryRepository
) {
    suspend operator fun invoke(): List<Category> {
        return categoryRepository.getTvShowCategories()
    }
}