package com.amsterdam.viewmodel.continueWatching

import com.amsterdam.viewmodel.shared.uiStates.media.MediaType

interface ContinueWatchingInteractionListener {
    fun onClickMediaItem(mediaId : Long, mediaType: MediaType)
    fun onClickBack()
    fun onClickRetryLoading()
}