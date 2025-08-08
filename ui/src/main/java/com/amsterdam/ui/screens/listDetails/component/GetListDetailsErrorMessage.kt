package com.amsterdam.ui.screens.listDetails.component

import android.content.Context
import com.amsterdam.ui.R
import com.amsterdam.viewmodel.listDetails.ListDetailsUiState.ListDetailsError

fun getListDetailsErrorMessage(listDetailsError: ListDetailsError?, context: Context): String{
    return when(listDetailsError){
        ListDetailsError.NoNetwork -> context.getString(R.string.offline_message)
        else -> ""
    }
}