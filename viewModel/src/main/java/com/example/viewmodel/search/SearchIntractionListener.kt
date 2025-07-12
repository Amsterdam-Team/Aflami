package com.example.viewmodel.search

import com.example.viewmodel.common.MovieGenreType
import com.example.viewmodel.common.TabOption

interface GlobalSearchInteractionListener {
    fun onNavigateBackClicked()
    fun onTextValuedChanged(text: String)
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
    fun onGenreButtonChanged(genreName: String)

    fun onApplyButtonClicked()
    fun onClearButtonClicked()
}