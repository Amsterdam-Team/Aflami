package com.amsterdam.viewmodel.continueWatching

import com.amsterdam.viewmodel.shared.uiStates.MediaType

interface ContinueWatchingInteractionListener {
    fun onClickMediaItem(mediaId: Long, mediaType: MediaType)
    fun onClickBack()
    fun onClickRetryLoading()
}