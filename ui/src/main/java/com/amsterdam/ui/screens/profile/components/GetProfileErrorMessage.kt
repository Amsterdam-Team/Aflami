package com.amsterdam.ui.screens.profile.components

import android.content.Context
import com.amsterdam.ui.R
import com.amsterdam.viewmodel.login.LoginErrorState
import com.amsterdam.viewmodel.profile.ProfileErrorState

fun getProfileErrorMessage(profileErrorState: ProfileErrorState?, context: Context): String{
    return when(profileErrorState){
        ProfileErrorState.UnknownError -> context.getString(R.string.search_error_unknown)
        else -> ""
    }
}