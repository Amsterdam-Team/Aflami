package com.example.viewmodel.search.globalSearch

import com.example.viewmodel.common.TabOption
import com.example.viewmodel.search.globalSearch.genre.MovieGenre
import com.example.viewmodel.search.globalSearch.genre.TvShowGenre

interface GlobalSearchInteractionListener {
    fun onNavigateBackClicked()
    fun onTextValuedChanged(text: String)
    fun onSearchActionClicked()
    fun onFilterButtonClicked()
    fun onWorldSearchCardClicked()
    fun onActorSearchCardClicked()
    fun onRetryQuestClicked()

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
    fun onMovieGenreButtonChanged(genreType: MovieGenre)
    fun onTvGenreButtonChanged(genreType: TvShowGenre)

    fun onApplyButtonClicked()
    fun onClearButtonClicked()
}