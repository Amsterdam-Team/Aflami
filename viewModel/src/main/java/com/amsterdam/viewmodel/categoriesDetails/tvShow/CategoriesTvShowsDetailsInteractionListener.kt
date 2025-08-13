package com.amsterdam.viewmodel.categoriesDetails.tvShow

import androidx.paging.CombinedLoadStates
import com.amsterdam.entity.category.TvShowGenre

interface CategoriesTvShowsDetailsInteractionListener {
    fun onClickBack()
    fun onClickTvShowCard(tvShowId: Long)
    fun onClickGenre(tvShowGenre: TvShowGenre)
    fun onClickRetryRequest()
    fun onPagingLoadStateChanged(loadStates: CombinedLoadStates)
}