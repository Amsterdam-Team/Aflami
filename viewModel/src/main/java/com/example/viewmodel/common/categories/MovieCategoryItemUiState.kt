package com.example.viewmodel.common.categories

import kotlin.collections.map

data class MovieCategoryItemUiState(
    val type: MovieCategoryType = MovieCategoryType.ALL,
    override val isSelected: Boolean = true
): BaseCategoryItemUiState {
    override val categoryType: MovieCategoryType
        get() = type

    companion object{
        fun List<MovieCategoryItemUiState>.selectByType(type: MovieCategoryType): List<MovieCategoryItemUiState> {
            return map { it.copy(isSelected = it.type == type) }
        }

        fun List<MovieCategoryItemUiState>.getSelectedOne(categories: List<MovieCategoryItemUiState>): MovieCategoryItemUiState {
            return this.first { it.isSelected == true }
        }
    }
}
