package com.amsterdam.ui.screens.seriesDetails.mappers

import android.content.Context
import com.amsterdam.ui.R

fun formatSeasonText(context: Context, seasonCount: Int): String {
    return context.resources.getQuantityString(
        R.plurals.season_count,
        seasonCount,
        seasonCount
    )
}