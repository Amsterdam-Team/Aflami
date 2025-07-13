package com.amsterdam.designsystem.components.globalSearchHub

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.amsterdam.designsystem.R
import com.amsterdam.designsystem.theme.AppTheme
import com.amsterdam.designsystem.theme.GradientType

enum class GlobalSearchHubUI(
    @StringRes val labelRes: Int,
    @StringRes val descriptionRes: Int,
    @DrawableRes val icon: Int,
    private val gradientProvider: GradientType
) {
    ACTOR(
        labelRes = R.string.find_by_actor,
        descriptionRes = R.string.find_by_actor_description_hint,
        icon = R.drawable.img_suggestion_magician,
        gradientProvider = { AppTheme.color.findByActorGradient }
    ),
    WORLD(
        labelRes = R.string.world_tour_title,
        descriptionRes = R.string.world_tour_description,
        icon = R.drawable.tour_world_image,
        gradientProvider = { AppTheme.color.worldTourGradient }
    );

    val gradient: List<Color>
        @Composable get() = gradientProvider()
}