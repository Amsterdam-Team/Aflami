package com.example.viewmodel.search.searchByKeyword

import com.example.viewmodel.common.TabOption
import com.example.viewmodel.search.searchByKeyword.genre.MovieGenre
import com.example.viewmodel.search.searchByKeyword.genre.TvShowGenre

interface SearchInteractionListener {
    fun onNavigateBackClicked()
    fun onKeywordValuedChanged(keyword: String)
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
}


interface FilterInteractionListener {
    fun onCancelButtonClicked()
    fun onRatingStarChanged(ratingIndex: Int)
    fun onMovieGenreButtonChanged(genreType: MovieGenre)
    fun onTvGenreButtonChanged(genreType: TvShowGenre)

    fun onApplyButtonClicked()
    fun onClearButtonClicked()
}