package com.amsterdam.viewmodel.profile

import com.amsterdam.domain.utils.RestrictionLevel

interface ProfileInteractionListener {
    fun onClickLogin()


    //region Settings
    fun onClickSettings()
    fun onDismissSettingsDialog()

    fun onClickLogout()
    fun onDismissLogoutDialog()

    fun onClickForgotPassword()

    fun onClickContentRestriction()
    fun onDismissContentRestrictionDialog()

    fun onClickConfirmLogout()

    fun onUpdateRestrictionLevel(restrictionLevel: RestrictionLevel)
    fun onSaveRestrictionLevel()
    //endregion
}