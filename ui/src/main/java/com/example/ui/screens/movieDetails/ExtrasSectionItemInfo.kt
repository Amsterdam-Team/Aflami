package com.example.ui.screens.movieDetails

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.designsystem.R
import com.example.viewmodel.movieDetails.MovieDetailsUiState.MovieExtras
import com.example.viewmodel.seriesDetails.SeriesDetailsUiState
import com.example.viewmodel.seriesDetails.SeriesDetailsUiState.SeriesExtras

data class ExtrasSectionItemInfo(
    @DrawableRes val iconResId: Int,
    @StringRes val textResId: Int,
)

fun MovieExtras.getExtrasSectionItemInfo(): ExtrasSectionItemInfo {
    return when (this) {
        MovieExtras.MORE_LIKE_THIS -> ExtrasSectionItemInfo(R.drawable.ic_camera_video, R.string.more_like_this)
        MovieExtras.REVIEWS -> ExtrasSectionItemInfo(R.drawable.ic_filled_star, R.string.reviews)
        MovieExtras.GALLERY -> ExtrasSectionItemInfo(R.drawable.ic_gallery, R.string.gallary)
        MovieExtras.COMPANY_PRODUCTION -> ExtrasSectionItemInfo(R.drawable.ic_company, R.string.company_production)
    }
}

fun SeriesExtras.getSeriesExtrasSectionItemInfo(): ExtrasSectionItemInfo {
    return when (this) {
        SeriesExtras.SEASONS -> ExtrasSectionItemInfo(R.drawable.ic_play_list, R.string.seasons)
        SeriesExtras.MORE_LIKE_THIS -> ExtrasSectionItemInfo(R.drawable.ic_camera_video, R.string.more_like_this)
        SeriesExtras.REVIEWS -> ExtrasSectionItemInfo(R.drawable.ic_filled_star, R.string.reviews)
        SeriesExtras.GALLERY -> ExtrasSectionItemInfo(R.drawable.ic_gallery, R.string.gallary)
        SeriesExtras.COMPANY_PRODUCTION -> ExtrasSectionItemInfo(R.drawable.ic_company, R.string.company_production)
    }
}