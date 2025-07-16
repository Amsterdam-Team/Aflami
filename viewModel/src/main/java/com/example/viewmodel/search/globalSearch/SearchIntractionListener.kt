package com.example.viewmodel.search.globalSearch

import com.example.entity.category.MovieGenre
import com.example.entity.category.TvShowGenre
import com.example.viewmodel.common.TabOption

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
    fun onRecentSearchCleared(keyword: String)
    fun onAllRecentSearchesCleared()
    fun onSearchCleared()
    fun onMovieClicked(movieId : Long)
}


interface FilterInteractionListener {
    fun onCancelButtonClicked()
    fun onRatingStarChanged(ratingIndex: Int)
    fun onMovieGenreButtonChanged(genreType: MovieGenre)
    fun onTvGenreButtonChanged(genreType: TvShowGenre)

    fun onApplyButtonClicked()
    fun onClearButtonClicked()
}