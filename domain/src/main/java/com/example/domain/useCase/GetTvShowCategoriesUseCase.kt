package com.example.domain.useCase

import com.example.domain.repository.CategoryRepository
import com.example.entity.Category

class GetTvShowCategoriesUseCase(
    private val categoryRepository: CategoryRepository
) {
    suspend operator fun invoke(): List<Category> {
        return categoryRepository.getTvShowCategories()
    }
}