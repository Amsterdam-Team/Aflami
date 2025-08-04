package com.amsterdam.ui.screens.onBoarding

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.amsterdam.ui.R

data class OnboardingScreenData(
    @DrawableRes val imageResId: Int,
    @StringRes val titleResId: Int,
    @StringRes val descriptionResId: Int
)

object OnboardingData {
    val screens = listOf(
        OnboardingScreenData(
            imageResId = R.drawable.bg_man_with_popcorn,
            titleResId = R.string.onboarding1_title,
            descriptionResId = R.string.onboarding1_description
        ),
        OnboardingScreenData(
            imageResId = R.drawable.bg_cinema_movie_theater,
            titleResId = R.string.onboarding2_title,
            descriptionResId = R.string.onboarding2_description
        ),
        OnboardingScreenData(
            imageResId = R.drawable.bg_directly_shot_film,
            titleResId = R.string.onboarding3_title,
            descriptionResId = R.string.onboarding3_description
        ),
        OnboardingScreenData(
            imageResId = R.drawable.bg_children_wearing_3d,
            titleResId = R.string.onboarding4_title,
            descriptionResId = R.string.onboarding4_description
        )
    )
}