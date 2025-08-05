package com.amsterdam.viewmodel.watchHistory

import com.amsterdam.viewmodel.search.keywordSearch.TabOption
import com.amsterdam.viewmodel.shared.uiStates.media.MediaType

interface WatchHistoryInteractionListener {
    fun onClickMediaItem(mediaId : Long, mediaType: MediaType)
    fun onClickBack()
    fun onClickRetryLoading()
    fun onClickTabOption(option: TabOption)
    fun onClickMovieCard(movieId : Long)
    fun onClickTvShowCard(tvShowId: Long)
}