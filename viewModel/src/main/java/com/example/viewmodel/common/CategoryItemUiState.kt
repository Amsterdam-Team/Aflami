package com.example.viewmodel.common

import com.example.viewmodel.search.mapper.CategoryType
import kotlin.collections.map

data class CategoryItemUiState(
    val type: CategoryType = CategoryType.ALL,
    val isSelected: Boolean = true
){
    companion object{
        fun List<CategoryItemUiState>.selectByType(type: CategoryType): List<CategoryItemUiState> {
            return map { it.copy(isSelected = it.type == type) }
        }

        fun List<CategoryItemUiState>.getSelectedOne(categories: List<CategoryItemUiState>): CategoryItemUiState {
            return this.first { it.isSelected == true }
        }
    }
}
