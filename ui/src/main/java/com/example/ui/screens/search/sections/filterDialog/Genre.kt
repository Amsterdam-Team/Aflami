package com.example.ui.screens.search.sections.filterDialog

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class Genre<T>(
    val type: T,
    @DrawableRes val icon: Int,
    @StringRes val displayableName: Int
)