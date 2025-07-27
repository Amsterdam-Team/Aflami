package com.amsterdam.viewmodel.search.keywordSearch

import androidx.paging.CombinedLoadStates
import com.amsterdam.entity.category.MovieGenre
import com.amsterdam.entity.category.TvShowGenre

interface SearchInteractionListener {
    fun onClickNavigateBack()
    fun onChangeSearchKeyword(keyword: String)
    fun onSaveSearchHistory()
    fun onClickFilterButton()
    fun onClickWorldSearchCard()
    fun onClickActorSearchCard()
    fun onClickRetryRequest()

    fun onClickTabOption(tabOption: TabOption)
    fun onClickMovieCard(movieId : Long)
    fun onClickTvShowCard(tvShowId: Long)

    fun onClickRecentSearch(keyword: String)
    fun onClickClearRecentSearch(keyword: String)
    fun onClickClearAllRecentSearches()
    fun onClickClearSearch()
    fun onPagingLoadStateChanged(loadStates: CombinedLoadStates)
}

interface FilterInteractionListener {
    fun onClickCancel()
    fun onChangeRatingStar(ratingIndex: Int)
    fun onChangeMovieGenre(genreType: MovieGenre)
    fun onChangeTvShowGenre(genreType: TvShowGenre)

    fun onClickApply()
    fun onClickClear()
}