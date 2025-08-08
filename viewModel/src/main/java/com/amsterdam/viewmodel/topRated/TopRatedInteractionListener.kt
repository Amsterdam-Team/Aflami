package com.amsterdam.viewmodel.topRated

import androidx.paging.CombinedLoadStates
import com.amsterdam.viewmodel.shared.uiStates.MediaType

interface TopRatedInteractionListener {
    fun onClickMediaItem(mediaId : Long, mediaType: MediaType)
    fun onClickBack()
    fun onClickRetryLoading()
    fun onPagingLoadStateChanged(loadStates: CombinedLoadStates)

}