package com.example.viewmodel.common.categories

import kotlin.collections.map

data class TvShowCategoryItemUiState(
    val type: TvShowCategoryType = TvShowCategoryType.ALL,
    override val isSelected: Boolean = true
): BaseCategoryItemUiState {
    override val categoryType: Any
        get() = type

    companion object{
        fun List<TvShowCategoryItemUiState>.selectByType(type: TvShowCategoryType): List<TvShowCategoryItemUiState> {
            return map { it.copy(isSelected = it.type == type) }
        }

        fun List<TvShowCategoryItemUiState>.getSelectedOne(categories: List<TvShowCategoryItemUiState>): TvShowCategoryItemUiState {
            return this.first { it.isSelected == true }
        }
    }
}
