package com.example.viewmodel.search

import com.example.viewmodel.common.TabOption
import com.example.viewmodel.search.mapper.CategoryType

interface GlobalSearchInteractionListener {
    fun onNavigateBackClicked()
    fun onTextValuedChanged(text: String)
    fun onSearchActionClicked()
    fun onFilterButtonClicked()
    fun onWorldSearchCardClicked()
    fun onActorSearchCardClicked()

    fun onTabOptionClicked(tabOption: TabOption)
    fun onMovieCardClicked()

    fun onRecentSearchClicked(keyword: String)
    fun onClearRecentSearch(keyword: String)
    fun onClearAllRecentSearches()
}


interface FilterInteractionListener {
    fun onCancelButtonClicked()
    fun onRatingStarChanged(ratingIndex: Int)
    fun onGenreButtonChanged(genreType: CategoryType)

    fun onApplyButtonClicked()
    fun onClearButtonClicked()
}