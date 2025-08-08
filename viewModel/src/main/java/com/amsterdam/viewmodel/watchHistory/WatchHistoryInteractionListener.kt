package com.amsterdam.viewmodel.watchHistory

import com.amsterdam.viewmodel.shared.TabOption
import com.amsterdam.viewmodel.shared.uiStates.MediaType

interface WatchHistoryInteractionListener {
    fun onClickMediaItem(mediaId : Long, mediaType: MediaType)
    fun onClickBack()
    fun onClickRetryLoading()
    fun onClickTabOption(option: TabOption)
    fun onClickMovieCard(movieId : Long)
    fun onClickTvShowCard(tvShowId: Long)
}