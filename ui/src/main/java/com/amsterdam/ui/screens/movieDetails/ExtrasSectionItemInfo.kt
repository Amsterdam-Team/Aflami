package com.amsterdam.ui.screens.movieDetails

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.amsterdam.ui.R
import com.amsterdam.viewmodel.movieDetails.MovieDetailsUiState.MovieExtras
import com.amsterdam.viewmodel.seriesDetails.SeriesDetailsUiState.SeriesExtras

data class ExtrasSectionItemInfo(
    @DrawableRes val iconResId: Int,
    @StringRes val textResId: Int,
)

fun MovieExtras.getExtrasSectionItemInfo(): ExtrasSectionItemInfo {
    return when (this) {
        MovieExtras.MORE_LIKE_THIS -> ExtrasSectionItemInfo(com.amsterdam.designsystem.R.drawable.ic_camera_video, R.string.more_like_this)
        MovieExtras.REVIEWS -> ExtrasSectionItemInfo(com.amsterdam.designsystem.R.drawable.ic_filled_star, R.string.reviews)
        MovieExtras.GALLERY -> ExtrasSectionItemInfo(com.amsterdam.designsystem.R.drawable.ic_gallery, R.string.gallary)
        MovieExtras.COMPANY_PRODUCTION -> ExtrasSectionItemInfo(com.amsterdam.designsystem.R.drawable.ic_company, R.string.company_production)
    }
}

fun SeriesExtras.getSeriesExtrasSectionItemInfo(): ExtrasSectionItemInfo {
    return when (this) {
        SeriesExtras.SEASONS -> ExtrasSectionItemInfo(com.amsterdam.designsystem.R.drawable.ic_play_list, R.string.seasons)
        SeriesExtras.MORE_LIKE_THIS -> ExtrasSectionItemInfo(com.amsterdam.designsystem.R.drawable.ic_camera_video, R.string.more_like_this)
        SeriesExtras.REVIEWS -> ExtrasSectionItemInfo(com.amsterdam.designsystem.R.drawable.ic_filled_star, R.string.reviews)
        SeriesExtras.GALLERY -> ExtrasSectionItemInfo(com.amsterdam.designsystem.R.drawable.ic_gallery, R.string.gallary)
        SeriesExtras.COMPANY_PRODUCTION -> ExtrasSectionItemInfo(com.amsterdam.designsystem.R.drawable.ic_company, R.string.company_production)
    }
}