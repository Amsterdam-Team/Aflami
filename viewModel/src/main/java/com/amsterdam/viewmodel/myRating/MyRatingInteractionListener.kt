package com.amsterdam.viewmodel.myRating

import com.amsterdam.viewmodel.shared.TabOption

interface MyRatingInteractionListener {
    fun onClickNavigateBack()
    fun onChangeTabOption(tabOption: TabOption)

    fun onClickMovieCard(movieId: Long)
    fun onClickDeleteMyMovieRatingIcon(movieId: Long)

    fun onClickTvShowCard(tvShowId: Long)
    fun onClickDeleteMyTvShowRatingIcon(tvShowId: Long)

    fun onClickRetryRequest()
}