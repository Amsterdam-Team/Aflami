package com.example.ui.screens.movieDetails

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.designsystem.R
import com.example.viewmodel.movieDetails.Extras

data class ExtrasSectionItemInfo(
    @DrawableRes val iconResId: Int,
    @StringRes val textResId: Int,
)

fun Extras.getExtrasSectionItemInfo(): ExtrasSectionItemInfo {
    return when (this) {
        Extras.MORE_LIKE_THIS -> ExtrasSectionItemInfo(R.drawable.ic_camera_video, R.string.more_like_this)
        Extras.REVIEWS -> ExtrasSectionItemInfo(R.drawable.ic_filled_star, R.string.reviews)
        Extras.GALLERY -> ExtrasSectionItemInfo(R.drawable.ic_gallery, R.string.gallary)
        Extras.COMPANY_PRODUCTION -> ExtrasSectionItemInfo(R.drawable.ic_company, R.string.company_production)
    }
}