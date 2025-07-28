package com.amsterdam.viewmodel.topRated

import com.amsterdam.viewmodel.shared.uiStates.media.MediaType

interface TopRatedInteractionListener {
    fun onClickMediaItem(mediaId : Long, mediaType: MediaType)
    fun onClickBack()
    fun onClickRetryLoading()
}