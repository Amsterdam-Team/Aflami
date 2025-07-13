package com.amsterdam.domain.useCase

import com.amsterdam.domain.repository.CategoryRepository
import com.amsterdam.entity.Category

class GetTvShowCategoriesUseCase(
    private val categoryRepository: CategoryRepository
) {
    suspend operator fun invoke(): List<Category> {
        return categoryRepository.getTvShowCategories()
    }
}