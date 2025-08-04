package com.amsterdam.viewmodel.listDetails

interface ListDetailsInteractionListener {
    fun onClickMovie(movieId: Long)
    fun onClickBack()
    fun onClickRetryLoading()
    fun onClickDeleteList()
    fun onDeleteListConfirmed()
    fun onDeleteListDialogDismiss()
    fun onClickRemoveMovie(movieId: Long)
}