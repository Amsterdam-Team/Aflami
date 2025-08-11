package com.amsterdam.viewmodel.listDetails

import androidx.paging.CombinedLoadStates

interface ListDetailsInteractionListener {
    fun onClickMovie(movieId: Long)
    fun onClickTvShow(tvShowId: Long)
    fun onClickBack()
    fun onClickRetryLoading()
    fun onClickDeleteList()
    fun onDeleteListConfirmed()
    fun onDeleteListDialogDismiss()
    fun onClickRemoveMovie(movieId: Long)
    fun onPagingLoadStateChanged(loadStates: CombinedLoadStates)
}