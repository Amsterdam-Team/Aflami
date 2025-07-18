package com.example.viewmodel.search.keywordSearch

import com.example.entity.category.MovieGenre
import com.example.entity.category.TvShowGenre

interface SearchInteractionListener {
    fun onClickNavigateBack()
    fun onChangeSearchKeyword(keyword: String)
    fun onClickSearchAction()
    fun onClickFilterButton()
    fun onClickWorldSearchCard()
    fun onClickActorSearchCard()
    fun onClickRetryRequest()

    fun onClickTabOption(tabOption: TabOption)
    fun onClickMovieCard(movieId : Long)

    fun onClickRecentSearch(keyword: String)
    fun onClickClearRecentSearch(keyword: String)
    fun onClickClearAllRecentSearches()
    fun onClickClearSearch()
}

interface FilterInteractionListener {
    fun onClickCancel()
    fun onChangeRatingStar(ratingIndex: Int)
    fun onChangeMovieGenre(genreType: MovieGenre)
    fun onChangeTvShowGenre(genreType: TvShowGenre)

    fun onClickApply()
    fun onClickClear()
}