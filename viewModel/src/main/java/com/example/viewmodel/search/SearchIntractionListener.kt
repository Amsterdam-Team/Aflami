package com.example.viewmodel.search

import com.example.viewmodel.common.TabOption
import com.example.viewmodel.common.categories.MovieCategoryType
import com.example.viewmodel.common.categories.TvShowCategoryType

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
    fun onClearSearch()
}


interface FilterInteractionListener {
    fun onCancelButtonClicked()
    fun onRatingStarChanged(ratingIndex: Int)
    fun onMovieGenreButtonChanged(genreType: MovieCategoryType)
    fun onTvGenreButtonChanged(genreType: TvShowCategoryType)

    fun onApplyButtonClicked()
    fun onClearButtonClicked()
}