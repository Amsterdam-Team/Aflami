package com.amsterdam.viewmodel.profile

sealed interface ProfileEffect {
    data object LanguageChanged : ProfileEffect
    data object ThemeChanged : ProfileEffect
    data object LanguageNotChanged : ProfileEffect

    data object ThemeNotChanged : ProfileEffect
    data object NavigateToLogin : ProfileEffect
    data object NavigateToResetPassword : ProfileEffect

    data object NavigateToMyRating : ProfileEffect
    data object ShowError : ProfileEffect
    data object UserDataNotLoaded : ProfileEffect

    data object ShowRestrictionLevelUpdateSuccessSnackBar : ProfileEffect
    data object ShowRestrictionLevelUpdateErrorSnackBar : ProfileEffect
}