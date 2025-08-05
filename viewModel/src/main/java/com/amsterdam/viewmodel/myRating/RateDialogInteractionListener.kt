package com.amsterdam.viewmodel.myRating

interface RateDialogInteractionListener {
    fun onClickCancel()
    fun onClickSubmit()
    fun onChangeRating(newRate: Int)
}