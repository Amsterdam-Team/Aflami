package com.amsterdam.viewmodel.myRating

interface RateDialogInteractionListener {
    fun onClickCancelRateDialog()
    fun onClickSubmit()
    fun onChangeRating(newRate: Int)
}